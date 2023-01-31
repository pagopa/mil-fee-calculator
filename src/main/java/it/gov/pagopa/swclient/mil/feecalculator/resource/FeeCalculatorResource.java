/**
 * This module contains the REST endpoints exposed by the microservice.
 */
package it.gov.pagopa.swclient.mil.feecalculator.resource;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import it.gov.pagopa.swclient.mil.bean.CommonHeader;
import it.gov.pagopa.swclient.mil.bean.Errors;
import it.gov.pagopa.swclient.mil.feecalculator.ErrorCode;
import it.gov.pagopa.swclient.mil.feecalculator.bean.FeeBody;
import it.gov.pagopa.swclient.mil.feecalculator.bean.FeeResponse;
import it.gov.pagopa.swclient.mil.feecalculator.bean.FeeServiceBody;
import it.gov.pagopa.swclient.mil.feecalculator.bean.Notice;
import it.gov.pagopa.swclient.mil.feecalculator.bean.Transfer;
import it.gov.pagopa.swclient.mil.feecalculator.bean.TransferForFeeService;
import it.gov.pagopa.swclient.mil.feecalculator.client.FeeService;


@Path("/fees")
public class FeeCalculatorResource {
	
	@RestClient
	private FeeService feeService;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Uni<Response> getFees(@Valid @BeanParam CommonHeader headers, @Valid FeeBody body) {
		Log.debugf("getFees - Input parameters: %s, body %s", headers, body);
		
		FeeServiceBody feeServiceBody = mapServiceBody(body);
		
		return feeService.getFees(feeServiceBody)
			.onFailure().transform(f -> {
				
					Log.errorf(f, "[%s] Error while retrieving fees ", ErrorCode.ERROR_RETRIEVING_FEES);
					return new InternalServerErrorException(Response
						.status(Status.INTERNAL_SERVER_ERROR)
						.entity(new Errors(List.of(ErrorCode.ERROR_RETRIEVING_FEES)))
						.build());
			})
			
			.map(f -> {
				FeeResponse response = new FeeResponse();
				response.setFee(String.valueOf(f.get(0).getTaxPayerFee()));
				return Response.status(Status.OK).entity(response).build();
			});
	
	}
	
	private FeeServiceBody mapServiceBody(FeeBody body) {
		//for the POC only one value in the body is considered
		Notice notice = body.getNotices().get(0);
		FeeServiceBody feeServiceBody = new FeeServiceBody();
		feeServiceBody.setPaymentAmount(notice.getAmount());
		feeServiceBody.setPrimaryCreditorIntitution(notice.getPaTaxCode());
		feeServiceBody.setPaymentMethod(body.getPaymentMethod());
		List<TransferForFeeService> transferList = new ArrayList<>();
		for (Transfer transfer : notice.getTransfers()) {
			TransferForFeeService t = new TransferForFeeService();
			t.setCreditorInstitution(transfer.getPaTaxCode());
			t.setTranferCategory(transfer.getCategory());
			transferList.add(t);
		}
		feeServiceBody.setTransferList(transferList);
		return feeServiceBody;
	}
}