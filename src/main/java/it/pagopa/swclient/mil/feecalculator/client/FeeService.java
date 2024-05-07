package it.pagopa.swclient.mil.feecalculator.client;

import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.smallrye.mutiny.Uni;
import it.pagopa.swclient.mil.feecalculator.client.bean.GecGetFeesRequest;
import it.pagopa.swclient.mil.feecalculator.client.bean.GecGetFeesResponse;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

/**
 * Reactive rest client for the REST APIs exposed by GEC
 */
@Path("/fees")
@RegisterRestClient(configKey = "fees-api")
public interface FeeService {

	/**
	 * Client of the getFees API exposed by GEC
	 *
	 * @param requestId         the requestId passed by the client
	 * @param gecGetFeesRequest the request to GEC
	 * @return the response from GEC
	 */
	@POST
	@ClientHeaderParam(name = "maxOccurrences", value = "${fees.max.occurrences}", required = false)
	@ClientHeaderParam(name = "Ocp-Apim-Subscription-Key", value = "${ocp.apim.subscription}", required = false)
	Uni<GecGetFeesResponse> getFees(@HeaderParam("X-Request-Id") String requestId, GecGetFeesRequest gecGetFeesRequest);

}