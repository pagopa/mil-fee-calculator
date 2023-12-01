/**
 * This module contains the REST endpoints exposed by the microservice.
 */
package it.pagopa.swclient.mil.feecalculator.resource;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import it.pagopa.swclient.mil.bean.CommonHeader;
import it.pagopa.swclient.mil.bean.Errors;
import it.pagopa.swclient.mil.feecalculator.ErrorCode;
import it.pagopa.swclient.mil.feecalculator.bean.GetFeeRequest;
import it.pagopa.swclient.mil.feecalculator.bean.GetFeeResponse;
import it.pagopa.swclient.mil.feecalculator.bean.Notice;
import it.pagopa.swclient.mil.feecalculator.bean.Transfer;
import it.pagopa.swclient.mil.feecalculator.client.AzureADRestClient;
import it.pagopa.swclient.mil.feecalculator.client.FeeService;
import it.pagopa.swclient.mil.feecalculator.client.MilRestService;
import it.pagopa.swclient.mil.feecalculator.client.bean.GecGetFeesRequest;
import it.pagopa.swclient.mil.feecalculator.client.bean.GecTransfer;
import it.pagopa.swclient.mil.feecalculator.client.bean.Psp;
import it.pagopa.swclient.mil.feecalculator.client.bean.PspConfiguration;
import it.pagopa.swclient.mil.feecalculator.util.FeeSelector;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


@Path("/fees")
public class FeeCalculatorResource {

	@ConfigProperty(name = "gec.paymentmethod.map")
	Map<String, String> gecPaymentMethodMap;

	@ConfigProperty(name = "gec.touchpoint.map")
	Map<String, String> gecTouchpointMap;

	@RestClient
	FeeService feeService;

	@Inject
	MilRestService milRestService;

	@RestClient
	AzureADRestClient azureADRestClient;

	@ConfigProperty(name = "azure-auth-api.identity")
	String identity;

	public static final String STORAGE = "https://storage.azure.com";

	private static final String BEARER = "Bearer ";
	
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
	@RolesAllowed({"NoticePayer", "SlavePos"})
	public Uni<Response> getFee(@Valid @BeanParam CommonHeader headers,
								@Valid @NotNull(message = "[" + ErrorCode.REQUEST_MUST_NOT_BE_EMPTY + "] request must not be empty") GetFeeRequest getFeeRequest) {
		Log.debugf("getFee - Input parameters: %s, body %s", headers, getFeeRequest);

		return retrievePspConfiguration(headers.getRequestId(), headers.getAcquirerId())
				.map(pspConfiguration -> createGecGetFeeRequest(getFeeRequest, pspConfiguration.getPsp(), headers.getChannel()))
				.chain(gecRequest -> {
					Log.debugf("Calling GEC service: requestId %s, body %s", headers.getRequestId(), gecRequest);
					return feeService.getFees(headers.getRequestId(), gecRequest)
							.onFailure().transform(f -> {
								Log.errorf(f, "[%s] Error while calling the GEC getFees service", ErrorCode.ERROR_RETRIEVING_FEES);
								return new InternalServerErrorException(Response
										.status(Status.INTERNAL_SERVER_ERROR)
										.entity(new Errors(List.of(ErrorCode.ERROR_RETRIEVING_FEES)))
										.build());
							})
							.map(getFeesResponse -> {
								Log.debugf("Received GEC response: %s", getFeesResponse);
								long fee;
								try {
									fee = FeeSelector.getFirstFee(getFeesResponse.getBundleOptions());
								} catch (NoSuchElementException e) {
									Log.errorf("[%s] No fee found for data in request", ErrorCode.NO_FEE_FOUND);
									return Response
											.status(Status.INTERNAL_SERVER_ERROR)
											.entity(new Errors(List.of(ErrorCode.NO_FEE_FOUND)))
											.build();
								}
								GetFeeResponse response = new GetFeeResponse();
								response.setFee(fee);
								Log.debugf("getFee - Response: %s", response);
								return Response.status(Status.OK).entity(response).build();
							});
				});
	
	}


	/**
	 * Create the request to be sent to GEC to retrieve the fees
	 *
	 * @param getFeeRequest the {@link GetFeeRequest} received by the client
	 * @param pspId the identifier of the PSP
	 * @return the {@link GecGetFeesRequest} to be sent to GE
	 */
	private GecGetFeesRequest createGecGetFeeRequest(GetFeeRequest getFeeRequest, String pspId, String channel) {

		Notice notice = getFeeRequest.getNotices().get(0); // TODO: change logic when GEC will expose the cart
		List<Psp> idPspList = new ArrayList<>();
		Psp psp = new Psp();
		psp.setIdPsp(pspId);
		idPspList.add(psp);

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
		if (getFeeRequest.getPaymentMethod() != null) {
			gecGetFeesRequest.setPaymentMethod(gecPaymentMethodMap.getOrDefault(getFeeRequest.getPaymentMethod(), "ANY"));
		}
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

		return azureADRestClient.getAccessToken(identity, STORAGE)
				.onFailure().transform(t -> {
					Log.errorf(t, "[%s] Error while calling Azure AD rest service", ErrorCode.ERROR_CALLING_AZUREAD_REST_SERVICES);

					return new InternalServerErrorException(Response
							.status(Response.Status.INTERNAL_SERVER_ERROR)
							.entity(new Errors(List.of(ErrorCode.ERROR_CALLING_AZUREAD_REST_SERVICES)))
							.build());
				}).chain(token -> {
					Log.debugf("FeeCalculatorResource -> retrievePspConfiguration: Azure AD service returned a 200 status, response token: [%s]", token);

					if (token.getToken() == null) {
						return Uni.createFrom().failure(new InternalServerErrorException(Response
								.status(Response.Status.INTERNAL_SERVER_ERROR)
								.entity(new Errors(List.of(ErrorCode.AZUREAD_ACCESS_TOKEN_IS_NULL)))
								.build()));
					}
					return milRestService.getPspConfiguration(BEARER + token.getToken(), acquirerId)
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
							.map(confResponse -> {
								Log.debugf("retrievePSPConfiguration - response: %s ",confResponse);
								return confResponse.getPspConfigForGetFeeAndClosePayment();
							});
				});
	}
}
