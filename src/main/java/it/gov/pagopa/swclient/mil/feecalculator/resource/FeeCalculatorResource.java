/**
 * This module contains the REST endpoints exposed by the microservice.
 */
package it.gov.pagopa.swclient.mil.feecalculator.resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import it.gov.pagopa.swclient.mil.bean.CommonHeader;
import it.gov.pagopa.swclient.mil.bean.Errors;
import it.gov.pagopa.swclient.mil.feecalculator.ErrorCode;
import it.gov.pagopa.swclient.mil.feecalculator.bean.FeeRequest;
import it.gov.pagopa.swclient.mil.feecalculator.bean.FeeResponse;
import it.gov.pagopa.swclient.mil.feecalculator.bean.Notice;
import it.gov.pagopa.swclient.mil.feecalculator.bean.Transfer;
import it.gov.pagopa.swclient.mil.feecalculator.bean.TransferForFeeService;
import it.gov.pagopa.swclient.mil.feecalculator.bean.gec.FeesGecRequest;
import it.gov.pagopa.swclient.mil.feecalculator.client.FeeService;
import it.gov.pagopa.swclient.mil.feecalculator.dao.PspConfRepository;


@Path("/fees")
public class FeeCalculatorResource {
	
	@RestClient
	private FeeService feeService;
	
	@Inject
	PspConfRepository pspConfRepository;
	
	/**
	 * API to retrieve the commissions fees. 
	 * It retrieves the idPsp value from the database and using it to build a request to GEC service to retrieve the Fees.
	 * The response is parsed and manage only the fee value, mapping it in the API response.
	 * @param headers a set of mandatory headers
	 * @param body containing the JSon data
	 * @return the fee in JSson format.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Uni<Response> getFee(@Valid @BeanParam CommonHeader headers, @Valid FeeRequest body) {
		Log.debugf("getFee - Input parameters: %s, body %s", headers, body);
		
		return findIdPsp(body, headers.getAcquirerId())
				.onFailure().transform(t-> 
				{
					Log.errorf(t, "[%s] Internal server errorError retrieving the idPsp value from DB", ErrorCode.ERROR_RETRIEVING_ID_PSP);
					return new InternalServerErrorException(Response
							.status(Status.INTERNAL_SERVER_ERROR)
							.entity(new Errors(List.of(ErrorCode.ERROR_RETRIEVING_ID_PSP)))
							.build());
				})
				.chain( p ->{
					Log.debugf("Calling GEC Service with RequestId [%s] and body [%s]", headers.getRequestId(), p.toString());
					return feeService.getFees(p, headers.getRequestId())
						.onFailure().transform(f -> {
								Log.errorf(f, "[%s] Error while retrieving fees ", ErrorCode.ERROR_RETRIEVING_FEES);
								return new InternalServerErrorException(Response
									.status(Status.INTERNAL_SERVER_ERROR)
									.entity(new Errors(List.of(ErrorCode.ERROR_RETRIEVING_FEES)))
									.build());
						})
						.map(f -> {
							
							FeeResponse response = new FeeResponse();
							//for the POC only one element is returned
							response.setFee(f.get(0).getTaxPayerFee());
							Log.debugf("getFee - response: %s", response.toString());
							return Response.status(Status.OK).entity(response).build();
						});
				});
	
	}
	
	/**
	 * Maps the request to send to GEC
	 * @param body
	 * @param idPsp
	 * @return FeesGecRequest
	 */
	private FeesGecRequest mapServiceBody(FeeRequest body, String idPsp ) {
		//for the POC only one value in the body is considered
		Notice notice = body.getNotices().get(0);
		FeesGecRequest feesGecRequest = new FeesGecRequest();
		List<String> idPspList = new ArrayList<>();
		idPspList.add(idPsp);
		
		feesGecRequest.setIdPspList(null);
		feesGecRequest.setPaymentAmount(notice.getAmount());
		feesGecRequest.setPrimaryCreditorIntitution(notice.getPaTaxCode());
		feesGecRequest.setPaymentMethod(body.getPaymentMethod());
		List<TransferForFeeService> transferList = new ArrayList<>();
		for (Transfer transfer : notice.getTransfers()) {
			TransferForFeeService t = new TransferForFeeService();
			t.setCreditorInstitution(transfer.getPaTaxCode());
			t.setTranferCategory(transfer.getCategory());
			transferList.add(t);
		}
		feesGecRequest.setTransferList(transferList);
		return feesGecRequest;
	}
	
	/**
	 * Retrieves the idPsp from the database.
	 * If the value is not present return an exception.
	 * Otherwise it maps the request to send to the GEC
	 * @param body
	 * @param acquirerId
	 * @return the FeesGecRequest to send to the GEC
	 */
	private Uni<FeesGecRequest> findIdPsp(FeeRequest body,String acquirerId) {
		Log.debugf("findIdPspList - find idPsp by AcquirerId : [%s]", acquirerId);
    	return pspConfRepository.findByIdOptional(acquirerId)
		 .onItem().transform(o -> o.orElseThrow(() -> 
		 			new NotFoundException(Response
						.status(Status.NOT_FOUND)
						.build())
				 ))
		 .map(t -> mapServiceBody(body,t.pspConfiguration.getPspId())) ; 
	}
}