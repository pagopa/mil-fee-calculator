package it.pagopa.swclient.mil.feecalculator.it;

import org.junit.jupiter.api.TestInstance;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.quarkus.test.junit.TestProfile;
import it.pagopa.swclient.mil.feecalculator.FeeSelectorTest;

@QuarkusIntegrationTest
@TestProfile(IntegrationTestProfile.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FeeSelectorTestIT extends FeeSelectorTest {

}
