package it.gov.pagopa.swclient.mil.feecalculator.client;

import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.smallrye.mutiny.Uni;
import it.gov.pagopa.swclient.mil.feecalculator.bean.FeeServiceBody;
import it.gov.pagopa.swclient.mil.feecalculator.bean.FeeServiceResponse;

@Path("/fees")
@RegisterRestClient(configKey = "fees-api")
public interface FeeService {
	
	@POST
    Uni<List<FeeServiceResponse>> getFees(FeeServiceBody body);

}
