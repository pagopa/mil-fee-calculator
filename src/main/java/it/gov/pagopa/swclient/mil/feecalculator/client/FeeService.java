package it.gov.pagopa.swclient.mil.feecalculator.client;

import java.util.List;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.smallrye.mutiny.Uni;
import it.gov.pagopa.swclient.mil.feecalculator.bean.gec.FeeGecResponse;
import it.gov.pagopa.swclient.mil.feecalculator.bean.gec.FeesGecRequest;

@Path("/fees")
@RegisterRestClient(configKey = "fees-api")
public interface FeeService {
	
	@POST
	@ClientHeaderParam(name = "maxOccurrences", value = "${fees.max.occurrences}", required = false)
	@ClientHeaderParam(name = "Ocp-Apim-Subscription-Key", value = "${ocp.apim.subscription}", required = false)
    Uni<List<FeeGecResponse>> getFees(FeesGecRequest body,@HeaderParam("X-Request-Id") String requestId);

}
