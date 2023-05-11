package it.pagopa.swclient.mil.feecalculator.it;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.quarkus.test.junit.TestProfile;
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

	Map<String, String> commonHeaders;

	@BeforeAll
	void createTestObjects() {

		// common headers
		commonHeaders = new HashMap<>();
		commonHeaders.put("RequestId", UUID.randomUUID().toString());
		commonHeaders.put("Version", "1.0.0");
		commonHeaders.put("AcquirerId", "4585625");
		commonHeaders.put("Channel", "POS");
		commonHeaders.put("TerminalId", "0aB9wXyZ");
		commonHeaders.put("MerchantId", "28405fHfk73x88D");
		commonHeaders.put("SessionId", UUID.randomUUID().toString());

	}

	@Test
	void testGetFees_200() {

		Transfer transfer = new Transfer();
		transfer.setCategory("");
		transfer.setPaTaxCode("77777777777");
		
		Notice notice = new Notice();
		notice.setAmount(10000L);
		notice.setPaTaxCode("77777777777");
		notice.setTransfers(List.of(transfer));

		GetFeeRequest getFeeRequest = new GetFeeRequest();
		getFeeRequest.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.name());
		getFeeRequest.setNotices(List.of(notice));
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(commonHeaders)
				.and()
				.body(getFeeRequest)
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
				.headers(commonHeaders)
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
				.headers(commonHeaders)
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
				.headers(commonHeaders)
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

		GetFeeRequest bodyRequest = new GetFeeRequest();
		bodyRequest.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.toString());
		
		Transfer transfer = new Transfer();
		transfer.setCategory("KTM");
		transfer.setPaTaxCode("15376371009");
		
		Notice notice = new Notice();
		notice.setAmount(1000L);
		notice.setPaTaxCode("15376371009");
		List<Notice> notices = new ArrayList<>();
		notices.add(notice);
		
		List<Transfer> transfers = new ArrayList<>();
		transfers.add(transfer);
		notice.setTransfers(transfers);
		bodyRequest.setNotices(notices);

		Response response = given()
				.contentType(ContentType.JSON)
				.headers("RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", "1.0.0",
						"AcquirerId", "458562",
						"Channel", "ATM",
						"TerminalId", "0aB9wXyZ")
				.and()
				.body(bodyRequest)
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
