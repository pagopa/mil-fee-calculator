package it.gov.pagopa.swclient.mil.feecalculator;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import it.gov.pagopa.swclient.mil.feecalculator.bean.FeeBody;
import it.gov.pagopa.swclient.mil.feecalculator.bean.PaymentMethods;
import it.gov.pagopa.swclient.mil.feecalculator.resource.FeeCalculatorResource;

@QuarkusIntegrationTest
@QuarkusTestResource(value=Initializer.class,restrictToAnnotatedClass = true)
@TestHTTPEndpoint(FeeCalculatorResource.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FeeCalculatorResourceTestIT {
	
	final static String SESSION_ID	= "a6a666e6-97da-4848-b568-99fedccb642c";
	final static String API_VERSION	= "1.0.0-alpha-a.b-c-somethinglong+build.1-aef.1-its-okay";
	
	@Test
	void testTermsAndConds_200() {
		FeeBody body = new FeeBody();
		body.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.toString());
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", API_VERSION,
						"AcquirerId", "4585625",
						"Channel", "ATM",
						"TerminalId", "0aB9wXyZ",
						"SessionId", SESSION_ID)
				.and()
				.body(body)
				.when()
				.post()
				.then()
				.extract()
				.response();
			
	        Assertions.assertEquals(200, response.statusCode());
	        Assertions.assertNotNull(response.jsonPath().getString("fee"));
	}
	
}
