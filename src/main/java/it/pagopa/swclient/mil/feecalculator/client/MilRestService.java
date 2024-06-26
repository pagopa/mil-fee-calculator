package it.pagopa.swclient.mil.feecalculator.client;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import it.pagopa.swclient.mil.feecalculator.client.bean.AcquirerConfiguration;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MilRestService {

	@RestClient
	@Inject
	MilRestResource milRestResource;

	@CacheResult(cacheName = "cache-role")
	public Uni<AcquirerConfiguration> getPspConfiguration(String authorization, String acquirerId) {
		return milRestResource.getPspConfiguration(authorization, acquirerId);
	}
}
