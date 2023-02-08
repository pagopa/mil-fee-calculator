package it.gov.pagopa.swclient.mil.feecalculator.it;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import it.gov.pagopa.swclient.mil.feecalculator.ErrorCode;
import it.gov.pagopa.swclient.mil.feecalculator.bean.GetFeeRequest;
import it.gov.pagopa.swclient.mil.feecalculator.bean.Notice;
import it.gov.pagopa.swclient.mil.feecalculator.bean.PaymentMethods;
import it.gov.pagopa.swclient.mil.feecalculator.bean.Transfer;
import it.gov.pagopa.swclient.mil.feecalculator.dao.PspConfEntity;
import it.gov.pagopa.swclient.mil.feecalculator.dao.PspConfiguration;
import it.gov.pagopa.swclient.mil.feecalculator.it.resource.Initializer;
import it.gov.pagopa.swclient.mil.feecalculator.it.resource.MongoTestResource;
import it.gov.pagopa.swclient.mil.feecalculator.resource.FeeCalculatorResource;

@QuarkusIntegrationTest
@QuarkusTestResource(value=Initializer.class,restrictToAnnotatedClass = true)
@QuarkusTestResource(value=MongoTestResource.class,restrictToAnnotatedClass = true)
@TestHTTPEndpoint(FeeCalculatorResource.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FeeCalculatorResourceTestIT {
	
	final static String SESSION_ID	= "a6a666e6-97da-4848-b568-99fedccb642c";
	final static String API_VERSION	= "1.0.0-alpha-a.b-c-somethinglong+build.1-aef.1-its-okay";
	
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
				.headers(
						"RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", API_VERSION,
						"AcquirerId", "6665626",
						"Channel", "ATM",
						"TerminalId", "0aB9wXyZ",
						"SessionId", SESSION_ID)
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
	void testGetFee_500_communicationWithGecfailed() {
		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod(PaymentMethods.CASH.toString());
		
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
		body.setNotices(notices);
		
		PspConfiguration pspConfiguration = new PspConfiguration();
		pspConfiguration.setPspId("AGID_01");
		pspConfiguration.setPspBroker("9084rt");
		pspConfiguration.setPspPassword("09876yoih");
		PspConfEntity pspConfEntity = new PspConfEntity();
		pspConfEntity.acquirerId = "987654";
		pspConfEntity.pspConfiguration = pspConfiguration;
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", API_VERSION,
						"AcquirerId", "6665626",
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
				.headers(
						"RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", API_VERSION,
						"AcquirerId", "5555626",
						"Channel", "ATM",
						"TerminalId", "0aB9wXyZ",
						"SessionId", SESSION_ID)
				.and()
				.body(bodyRequest)
				.when()
				.post()
				.then()
				.extract()
				.response();
			
		Assertions.assertEquals(500, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.ERROR_RETRIEVING_ID_PSP)); 
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
		notice.setPaTaxCode("15376371009");
		List<Notice> notices = new ArrayList<>();
		notices.add(notice);
		
		List<Transfer> transfers = new ArrayList<>();
		transfers.add(transfer);
		notice.setTransfers(transfers);
		bodyRequest.setNotices(notices);
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", API_VERSION,
						"AcquirerId", "6665626",
						"Channel", "ATM",
						"TerminalId", "0aB9wXyZ",
						"SessionId", SESSION_ID)
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
