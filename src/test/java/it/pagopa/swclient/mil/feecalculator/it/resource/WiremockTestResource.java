package it.pagopa.swclient.mil.feecalculator.it.resource;

import java.util.Map;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import com.google.common.collect.ImmutableMap;

import io.quarkus.test.common.DevServicesContext;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class WiremockTestResource implements QuarkusTestResourceLifecycleManager, DevServicesContext.ContextAware {

	private static final Logger logger = LoggerFactory.getLogger(WiremockTestResource.class);
	private static final String WIREMOCK_NETWORK_ALIAS = "wiremock-it";

	private GenericContainer<?> wiremockContainer;

	private DevServicesContext devServicesContext;

	public void setIntegrationTestContext(DevServicesContext devServicesContext) {
		this.devServicesContext = devServicesContext;
	}

	@Override
	public int order() {
		return 1;
	}

	@Override
	public Map<String, String> start() {

		logger.info("Starting WireMock container...");

		wiremockContainer = new GenericContainer<>(DockerImageName.parse("wiremock/wiremock:latest"))
			.withNetwork(getNetwork())
			.withNetworkAliases(WIREMOCK_NETWORK_ALIAS)
			.waitingFor(Wait.forListeningPort())
			.withExposedPorts(8080);

		wiremockContainer.withLogConsumer(new Slf4jLogConsumer(logger));
		wiremockContainer.setCommand("--verbose --local-response-templating");
		wiremockContainer.withFileSystemBind("./src/test/resources/it/wiremock/mappings", "/home/wiremock/mappings");
		wiremockContainer.withFileSystemBind("./target/generated-idp-files", "/home/wiremock/__files");

		wiremockContainer.start();

		final Integer exposedPort = wiremockContainer.getMappedPort(8080);
		devServicesContext.devServicesProperties().put("test.wiremock.exposed-port", exposedPort.toString());

		final String wiremockEndpoint = "http://" + WIREMOCK_NETWORK_ALIAS + ":" + 8080;

		// Pass the configuration to the application under test
		return ImmutableMap.of(
			"rest-client-fees-url", wiremockEndpoint,
			"mil.rest-service.url", wiremockEndpoint,
			"jwt-publickey-location", wiremockEndpoint + "/jwks.json");

	}

	// create a "fake" network using the same id as the one that will be used by Quarkus
	// using the network is the only way to make the withNetworkAliases work
	private Network getNetwork() {
		logger.info("devServicesContext.containerNetworkId() -> " + devServicesContext.containerNetworkId());
		return new Network() {
			@Override
			public String getId() {
				return devServicesContext.containerNetworkId().orElse(null);
			}

			@Override
			public void close() {

			}

			@Override
			public Statement apply(Statement statement, Description description) {
				return null;
			}
		};
	}

	@Override
	public void stop() {
		// Stop the needed container(s)
		if (wiremockContainer != null) {
			logger.info("Stopping WireMock container...");
			wiremockContainer.stop();
			logger.info("WireMock container stopped!");
		}
	}
}
