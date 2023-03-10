package it.gov.pagopa.swclient.mil.feecalculator.client;

import java.util.List;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.smallrye.mutiny.Uni;
import it.gov.pagopa.swclient.mil.feecalculator.client.bean.GecGetFeesResponse;
import it.gov.pagopa.swclient.mil.feecalculator.client.bean.GecGetFeesRequest;

/**
 * Reactive rest client for the REST APIs exposed by GEC
 */
@Path("/fees")
@RegisterRestClient(configKey = "fees-api")
public interface FeeService {

	/**
	 * Client of the getFees API exposed by GEC
	 * @param gecGetFeesRequest the request to GEC
	 * @param requestId the requestId passed by the client
	 * @return the response from GEC
	 */
	@POST
	@ClientHeaderParam(name = "maxOccurrences", value = "${fees.max.occurrences}", required = false)
	@ClientHeaderParam(name = "Ocp-Apim-Subscription-Key", value = "${ocp.apim.subscription}", required = false)
    Uni<List<GecGetFeesResponse>> getFees(GecGetFeesRequest gecGetFeesRequest, @HeaderParam("X-Request-Id") String requestId);

}