package it.gov.pagopa.swclient.mil.feecalculator.it;

import com.google.common.collect.ImmutableList;
import io.quarkus.test.junit.QuarkusTestProfile;
import it.gov.pagopa.swclient.mil.feecalculator.it.resource.WiremockTestResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntegrationTestProfile implements QuarkusTestProfile {

    @Override
    public Map<String, String> getConfigOverrides() {

        Map<String, String> configOverrides = new HashMap<>();

        configOverrides.put("feecalculator.quarkus-log-level", "DEBUG");
        configOverrides.put("feecalculator.app-log-level", "DEBUG");

        configOverrides.put("fees.service.connect-timeout", "3000");
        configOverrides.put("fees.service.read-timeout", "3000");
        configOverrides.put("ocp-apim-subscription", "abcde");

        configOverrides.put("mil.rest-service.connect-timeout", "3000");
        configOverrides.put("mil.rest-service.read-timeout", "3000");
        configOverrides.put("mil.rest-service.subscription-key", "abcde");
        configOverrides.put("mil.acquirer-conf.version", "1.0.0");

        return configOverrides;
    }

    @Override
    public List<TestResourceEntry> testResources() {
        return ImmutableList.of(
                new TestResourceEntry(WiremockTestResource.class)
        );
    }

    @Override
    public boolean disableGlobalTestResources() {
        return true;
    }

}
