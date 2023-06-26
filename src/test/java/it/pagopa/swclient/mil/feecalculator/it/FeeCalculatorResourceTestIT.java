package it.pagopa.swclient.mil.feecalculator.it;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.quarkus.test.junit.TestProfile;
import it.pagopa.swclient.mil.feecalculator.it.resource.InjectTokenGenerator;
import it.pagopa.swclient.mil.feecalculator.util.FeeCalculatorTestData;
import it.pagopa.swclient.mil.feecalculator.util.Role;
import it.pagopa.swclient.mil.feecalculator.util.TokenGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import it.pagopa.swclient.mil.feecalculator.ErrorCode;
import it.pagopa.swclient.mil.feecalculator.bean.GetFeeRequest;
import it.pagopa.swclient.mil.feecalculator.bean.Notice;
import it.pagopa.swclient.mil.feecalculator.bean.PaymentMethods;
import it.pagopa.swclient.mil.feecalculator.bean.Transfer;
import it.pagopa.swclient.mil.feecalculator.resource.FeeCalculatorResource;

@QuarkusIntegrationTest
@TestProfile(IntegrationTestProfile.class)
@TestHTTPEndpoint(FeeCalculatorResource.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FeeCalculatorResourceTestIT {

	@InjectTokenGenerator
	TokenGenerator tokenGenerator;

	Map<String, String> milHeaders;

	@BeforeAll
	void createTestObjects() {

		// common headers
		milHeaders = FeeCalculatorTestData.getMilHeaders(true, true);

	}

	@Test
	void testGetFees_200() {

		Response response = given()
				.contentType(ContentType.JSON)
				.headers(milHeaders)
				.and()
				.auth()
				.oauth2(tokenGenerator.getToken(Role.NOTICE_PAYER))
				.and()
				.body(FeeCalculatorTestData.getFeeRequest())
				.when()
				.post()
				.then()
				.extract()
				.response();
			
	        Assertions.assertEquals(200, response.statusCode());
	        Assertions.assertNotNull(response.jsonPath().getString("fee"));
			Assertions.assertEquals(50, response.jsonPath().getLong("fee"));
	}

	@Test
	void testGetFees_403_forbidden() {

		Response response = given()
				.contentType(ContentType.JSON)
				.headers(milHeaders)
				.and()
				.auth()
				.oauth2(tokenGenerator.getToken(Role.NODO))
				.and()
				.body(FeeCalculatorTestData.getFeeRequest())
				.when()
				.post()
				.then()
				.extract()
				.response();

		Assertions.assertEquals(403, response.statusCode());
		Assertions.assertEquals(0, response.body().asString().length());
	}

	@Test
	void testGetFee_500_gecEmptyBundleList() {

		Transfer transfer = new Transfer();
		transfer.setCategory("");
		transfer.setPaTaxCode("00000001000");

		Notice notice = new Notice();
		notice.setAmount(10000L);
		notice.setPaTaxCode("00000001000");
		notice.setTransfers(List.of(transfer));

		GetFeeRequest getFeeRequest = new GetFeeRequest();
		getFeeRequest.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.name());
		getFeeRequest.setNotices(List.of(notice));

		Response response = given()
				.contentType(ContentType.JSON)
				.headers(milHeaders)
				.and()
				.auth()
				.oauth2(tokenGenerator.getToken(Role.NOTICE_PAYER))
				.and()
				.body(getFeeRequest)
				.when()
				.post()
				.then()
				.extract()
				.response();

		Assertions.assertEquals(500, response.statusCode());
		Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.NO_FEE_FOUND));
		Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}

	@Test
	void testGetFee_500_gecIntegrationError() {

		Transfer transfer = new Transfer();
		transfer.setCategory("");
		transfer.setPaTaxCode("00000001001");

		Notice notice = new Notice();
		notice.setAmount(10000L);
		notice.setPaTaxCode("00000001001");
		notice.setTransfers(List.of(transfer));

		GetFeeRequest getFeeRequest = new GetFeeRequest();
		getFeeRequest.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.name());
		getFeeRequest.setNotices(List.of(notice));

		Response response = given()
				.contentType(ContentType.JSON)
				.headers(milHeaders)
				.and()
				.auth()
				.oauth2(tokenGenerator.getToken(Role.NOTICE_PAYER))
				.and()
				.body(getFeeRequest)
				.when()
				.post()
				.then()
				.extract()
				.response();

		Assertions.assertEquals(500, response.statusCode());
		Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.ERROR_RETRIEVING_FEES));
		Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_500_GecTimeout() {

		Transfer transfer = new Transfer();
		transfer.setCategory("");
		transfer.setPaTaxCode("00000001002");

		Notice notice = new Notice();
		notice.setAmount(10000L);
		notice.setPaTaxCode("00000001002");
		notice.setTransfers(List.of(transfer));

		GetFeeRequest getFeeRequest = new GetFeeRequest();
		getFeeRequest.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.name());
		getFeeRequest.setNotices(List.of(notice));

		Response response = given()
				.contentType(ContentType.JSON)
				.headers(milHeaders)
				.and()
				.auth()
				.oauth2(tokenGenerator.getToken(Role.NOTICE_PAYER))
				.and()
				.body(getFeeRequest)
				.when()
				.post()
				.then()
				.extract()
				.response();
			
        Assertions.assertEquals(500, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.ERROR_RETRIEVING_FEES));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_500_pspNotFound() {

		Response response = given()
				.contentType(ContentType.JSON)
				.headers(FeeCalculatorTestData.getMilHeaders(true, false))
				.and()
				.auth()
				.oauth2(tokenGenerator.getToken(Role.NOTICE_PAYER))
				.and()
				.body(FeeCalculatorTestData.getFeeRequest())
				.when()
				.post()
				.then()
				.extract()
				.response();
			
		Assertions.assertEquals(500, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.UNKNOWN_ACQUIRER_ID));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}

}
