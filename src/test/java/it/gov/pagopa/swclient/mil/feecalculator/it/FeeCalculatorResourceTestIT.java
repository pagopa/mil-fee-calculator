package it.gov.pagopa.swclient.mil.feecalculator.it;

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
import it.gov.pagopa.swclient.mil.feecalculator.ErrorCode;
import it.gov.pagopa.swclient.mil.feecalculator.bean.GetFeeRequest;
import it.gov.pagopa.swclient.mil.feecalculator.bean.Notice;
import it.gov.pagopa.swclient.mil.feecalculator.bean.PaymentMethods;
import it.gov.pagopa.swclient.mil.feecalculator.bean.Transfer;
import it.gov.pagopa.swclient.mil.feecalculator.resource.FeeCalculatorResource;

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
		commonHeaders.put("Version", "1.0.0-alpha-a.b-c-somethinglong+build.1-aef.1-its-okay");
		commonHeaders.put("AcquirerId", "4585625");
		commonHeaders.put("Channel", "ATM");
		commonHeaders.put("TerminalId", "0aB9wXyZ");
		commonHeaders.put("SessionId", UUID.randomUUID().toString());

	}

	@Test
	void testGetFees_200() {
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
				.headers(commonHeaders)
				.and()
				.body(bodyRequest)
				.when()
				.post()
				.then()
				
				.extract()
				.response();
			
	        Assertions.assertEquals(200, response.statusCode());
	        Assertions.assertNotNull(response.jsonPath().getString("fee"));
	     
	}
	
	@Test
	void testGetFee_500_communicationWithGecFailed() {

		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod(PaymentMethods.CASH.toString());
		
		Transfer transfer = new Transfer();
		transfer.setCategory("KTM");
		transfer.setPaTaxCode("15376371009");
		
		Notice notice = new Notice();
		notice.setAmount(1000L);
		notice.setPaTaxCode("44476371009");
		List<Notice> notices = new ArrayList<>();
		notices.add(notice);
		
		List<Transfer> transfers = new ArrayList<>();
		transfers.add(transfer);
		notice.setTransfers(transfers);
		body.setNotices(notices);

		Response response = given()
				.contentType(ContentType.JSON)
				.headers(commonHeaders)
				.and()
				.body(body)
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
	
	
	@Test
	void testGetFee_500_TimeoutCallingGec() {
		GetFeeRequest bodyRequest = new GetFeeRequest();
		bodyRequest.setPaymentMethod(PaymentMethods.DEBIT_CARD.toString());
		
		Transfer transfer = new Transfer();
		transfer.setCategory("KTM");
		transfer.setPaTaxCode("15376371009");
		
		Notice notice = new Notice();
		notice.setAmount(1000L);
		notice.setPaTaxCode("55576371009");
		List<Notice> notices = new ArrayList<>();
		notices.add(notice);
		
		List<Transfer> transfers = new ArrayList<>();
		transfers.add(transfer);
		notice.setTransfers(transfers);
		bodyRequest.setNotices(notices);
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(commonHeaders)
				.and()
				.body(bodyRequest)
				.when()
				.post()
				.then()
				.extract()
				.response();
			
		Assertions.assertEquals(500, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.ERROR_RETRIEVING_FEES)); 
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
}
