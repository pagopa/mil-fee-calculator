package it.gov.pagopa.swclient.mil.feecalculator.client;

import io.smallrye.mutiny.Uni;
import it.gov.pagopa.swclient.mil.feecalculator.client.bean.AcquirerConfiguration;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * Reactive rest client for the REST APIs exposed by the MIL APIM
 */
@RegisterRestClient(configKey = "mil-rest-api")
public interface MilRestService {

	/**
	 * Retrieves the psp configuration
	 * @param requestId the id of the request
	 * @param acquirerId the acquirer id passed in request
	 * @return the psp configuration for the acquirer id
	 */
	@GET
	@Path("/mil-acquirer-conf/confs/{acquirerId}/psp")
	@ClientHeaderParam(name = "Ocp-Apim-Subscription-Key", value = "${mil.rest-service.subscription-key}", required = false)
	@ClientHeaderParam(name = "Version", value = "${mil.acquirer-conf.version}", required = false)
    Uni<AcquirerConfiguration> getPspConfiguration(@HeaderParam(value = "RequestId") String requestId, @PathParam(value = "acquirerId") String acquirerId);

}