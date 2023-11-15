package it.pagopa.swclient.mil.feecalculator.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import io.smallrye.mutiny.Uni;
import it.pagopa.swclient.mil.feecalculator.client.bean.AcquirerConfiguration;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

/**
 * Reactive rest client for the REST APIs exposed by the MIL APIM
 */
@RegisterRestClient(configKey = "mil-rest-api")
public interface MilRestResource {

	/**
	 * Retrieves the psp configuration
	 * @param acquirerId the acquirer id passed in request
	 * @return the psp configuration for the acquirer id
	 */
	@GET
	@Path("/acquirers/{acquirerId}.json")
	Uni<AcquirerConfiguration> getPspConfiguration(@PathParam(value = "acquirerId") String acquirerId);

}