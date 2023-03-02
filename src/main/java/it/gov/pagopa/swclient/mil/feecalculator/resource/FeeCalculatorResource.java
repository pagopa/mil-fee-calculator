/**
 * This module contains the REST endpoints exposed by the microservice.
 */
package it.gov.pagopa.swclient.mil.feecalculator.resource;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import it.gov.pagopa.swclient.mil.bean.CommonHeader;
import it.gov.pagopa.swclient.mil.bean.Errors;
import it.gov.pagopa.swclient.mil.feecalculator.ErrorCode;
import it.gov.pagopa.swclient.mil.feecalculator.bean.GetFeeRequest;
import it.gov.pagopa.swclient.mil.feecalculator.bean.GetFeeResponse;
import it.gov.pagopa.swclient.mil.feecalculator.bean.Notice;
import it.gov.pagopa.swclient.mil.feecalculator.bean.Transfer;
import it.gov.pagopa.swclient.mil.feecalculator.client.FeeService;
import it.gov.pagopa.swclient.mil.feecalculator.client.MilRestService;
import it.gov.pagopa.swclient.mil.feecalculator.client.bean.AcquirerConfiguration;
import it.gov.pagopa.swclient.mil.feecalculator.client.bean.GecGetFeesRequest;
import it.gov.pagopa.swclient.mil.feecalculator.client.bean.GecGetFeesResponse;
import it.gov.pagopa.swclient.mil.feecalculator.client.bean.GecTransfer;
import it.gov.pagopa.swclient.mil.feecalculator.client.bean.PspConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Path("/fees")
public class FeeCalculatorResource {

	@ConfigProperty(name = "gec.paymentmethod.map")
	Map<String, String> gecPaymentMethodMap;

	@ConfigProperty(name = "gec.touchpoint.map")
	Map<String, String> gecTouchpointMap;

	@RestClient
	FeeService feeService;
	
	@RestClient
	MilRestService milRestService;
	
	/**
	 * API to retrieve the commissions fees. 
	 * It retrieves the PSP id value from an API exposed by MIL and invokes the GEC service to retrieve the fees.
	 *
	 * @param headers a set of mandatory headers
	 * @param getFeeRequest the {@link GetFeeRequest} containing the payment notices for which to retrieve the fees
	 * @return a {@link GetFeeResponse} instance containing the fee data from GEC
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Uni<Response> getFee(@Valid @BeanParam CommonHeader headers, @Valid GetFeeRequest getFeeRequest) {
		Log.debugf("getFee - Input parameters: %s, body %s", headers, getFeeRequest);

		return retrievePspConfiguration(headers.getRequestId(), headers.getAcquirerId())
				.map(pspConfiguration -> createGecGetFeeRequest(getFeeRequest, pspConfiguration.getPsp(), headers.getChannel()))
				.chain(gecRequest -> {
					Log.debugf("Calling GEC Service with RequestId [%s] and body [%s]", headers.getRequestId(), gecRequest);
					return feeService.getFees(gecRequest, headers.getRequestId())
							.onFailure().transform(f -> {
								Log.errorf(f, "[%s] Error while calling the GEC getFees service", ErrorCode.ERROR_RETRIEVING_FEES);
								return new InternalServerErrorException(Response
										.status(Status.INTERNAL_SERVER_ERROR)
										.entity(new Errors(List.of(ErrorCode.ERROR_RETRIEVING_FEES)))
										.build());
							})
							.map(getFeesResponse -> {
								GetFeeResponse response = new GetFeeResponse();
								response.setFee(chooseFee(getFeesResponse, getFeeRequest.getPaymentMethod(), headers.getChannel()));
								Log.debugf("getFee - response: %s", response);
								return Response.status(Status.OK).entity(response).build();
							});
				});
	
	}

	/**
	 * Select the correct fee to return to the client based on the payment method and channel passed in request
	 *
	 * @param getFeesResponse the response of the getFees API on GEC
	 * @param paymentMethod the payment method passed in request by the client
	 * @param channel the channel passed in the headers by the client
	 * @return the fee value
	 */
	private Long chooseFee(List<GecGetFeesResponse> getFeesResponse, String paymentMethod, String channel) {

		// TODO: select correct fee by channel and paymentMethod
		Log.debugf("Choose fee to return to the client ");
		Optional<GecGetFeesResponse> getFeeResponseOpt =  getFeesResponse.stream().filter(fee ->
				(StringUtils.equals(fee.getPaymentMethod(), gecPaymentMethodMap.getOrDefault(paymentMethod, "ANY")) &&
						StringUtils.equals(fee.getTouchpoint(), gecTouchpointMap.getOrDefault(channel, "ANY")))).findFirst();

		if (getFeeResponseOpt.isPresent()) return getFeeResponseOpt.get().getTaxPayerFee();
		else {
			// search for a default
			getFeeResponseOpt =  getFeesResponse.stream().filter(fee ->
					(StringUtils.equals(fee.getPaymentMethod(), "ANY") && StringUtils.equals(fee.getTouchpoint(), "ANY"))).findFirst();
			if (getFeeResponseOpt.isPresent()) return getFeeResponseOpt.get().getTaxPayerFee();
			else {
				Log.errorf("[%s] Error choosing fee response ",ErrorCode.ERROR_CHOOSING_FEE_RESPONSE);
				throw new InternalServerErrorException(Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity(new Errors(List.of(ErrorCode.ERROR_CHOOSING_FEE_RESPONSE)))
					.build());
			}
		}

	}

	/**
	 * Create the request to be sent to GEC to retrieve the fees
	 *
	 * @param getFeeRequest the {@link GetFeeRequest} received by the client
	 * @param pspId the identifier of the PSP
	 * @return the {@link GecGetFeesRequest} to be sent to GE
	 */
	private GecGetFeesRequest createGecGetFeeRequest(GetFeeRequest getFeeRequest, String pspId, String channel) {

		Notice notice = getFeeRequest.getNotices().get(0); // TODO: change logic when or if GEC will expose the cart

		List<String> idPspList = new ArrayList<>();
		idPspList.add(pspId);

		List<GecTransfer> transferList = new ArrayList<>();
		for (Transfer transfer : notice.getTransfers()) {
			GecTransfer gecTransfer = new GecTransfer();
			gecTransfer.setCreditorInstitution(transfer.getPaTaxCode());
			if (StringUtils.isNotEmpty(transfer.getCategory())) {
				// the closePayment API of the node does not return a category
				// so the mil-payment-notice return an empty string
				// and will not forward this value to the GEC
				gecTransfer.setTransferCategory(transfer.getCategory());
			}
			transferList.add(gecTransfer);
		}

		GecGetFeesRequest gecGetFeesRequest = new GecGetFeesRequest();
		gecGetFeesRequest.setIdPspList(idPspList);
		gecGetFeesRequest.setPaymentAmount(notice.getAmount());
		gecGetFeesRequest.setPrimaryCreditorInstitution(notice.getPaTaxCode());
		// remapping paymentMethod and touchpoint based on property
		gecGetFeesRequest.setPaymentMethod(gecPaymentMethodMap.getOrDefault(getFeeRequest.getPaymentMethod(), "ANY"));
		gecGetFeesRequest.setTouchpoint(gecTouchpointMap.getOrDefault(channel, "ANY"));
		gecGetFeesRequest.setTransferList(transferList);

		return gecGetFeesRequest;
	}


	/**
	 * Retrieves the PSP configuration by acquirer id, and emits it as a Uni
	 *
	 * @param requestId the id of the request passed in request
	 * @param acquirerId the id of the acquirer
	 * @return the {@link Uni} emitting a {@link PspConfiguration}
	 */
	private Uni<PspConfiguration> retrievePspConfiguration(String requestId, String acquirerId) {
		Log.debugf("retrievePSPConfiguration - requestId: %s acquirerId: %s ", requestId, acquirerId);

		return milRestService.getPspConfiguration(requestId, acquirerId)
				.onFailure().transform(t -> {
					if (t instanceof ClientWebApplicationException webEx && webEx.getResponse().getStatus() == 404) {
						Log.errorf(t, "[%s] Missing psp configuration for acquirerId", ErrorCode.UNKNOWN_ACQUIRER_ID);
						return new InternalServerErrorException(Response
								.status(Response.Status.INTERNAL_SERVER_ERROR)
								.entity(new Errors(List.of(ErrorCode.UNKNOWN_ACQUIRER_ID)))
								.build());
					}
					else {
						Log.errorf(t, "[%s] Error retrieving the psp configuration", ErrorCode.ERROR_CALLING_MIL_REST_SERVICES);
						return new InternalServerErrorException(Response
								.status(Response.Status.INTERNAL_SERVER_ERROR)
								.entity(new Errors(List.of(ErrorCode.ERROR_CALLING_MIL_REST_SERVICES)))
								.build());
					}
				})
				.map(AcquirerConfiguration::getPspConfigForGetFeeAndClosePayment);
	}
}