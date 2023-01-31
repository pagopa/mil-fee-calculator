package it.gov.pagopa.swclient.mil.feecalculator;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.InternalServerErrorException;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.smallrye.mutiny.Uni;
import it.gov.pagopa.swclient.mil.feecalculator.bean.FeeBody;
import it.gov.pagopa.swclient.mil.feecalculator.bean.FeeServiceBody;
import it.gov.pagopa.swclient.mil.feecalculator.bean.FeeServiceResponse;
import it.gov.pagopa.swclient.mil.feecalculator.bean.Notice;
import it.gov.pagopa.swclient.mil.feecalculator.bean.PaymentMethods;
import it.gov.pagopa.swclient.mil.feecalculator.bean.Transfer;
import it.gov.pagopa.swclient.mil.feecalculator.client.FeeService;
import it.gov.pagopa.swclient.mil.feecalculator.resource.FeeCalculatorResource;

@QuarkusTest
@TestHTTPEndpoint(FeeCalculatorResource.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FeeCalculatorResourceTest {
	
	final static String SESSION_ID	= "a6a666e6-97da-4848-b568-99fedccb642c";
	final static String TAX_CODE	= "CHCZLN73D08A662B";
	final static String OUTCOME		= "TERMS_AND_CONDITIONS_NOT_YET_ACCEPTED";
	final static String TOKEN		= "XYZ13243XXYYZZ";
	final static String API_VERSION	= "1.0.0-alpha-a.b-c-somethinglong+build.1-aef.1-its-okay";
	final static String TC_VERSION	= "1";
	
	@InjectMock
	@RestClient
	private FeeService feeService;
	
	
	@Test
	void testTermsAndConds_200() {
		FeeBody body = new FeeBody();
		body.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.toString());
		
		Transfer transfer = new Transfer();
		transfer.setCategory("KTM");
		transfer.setPaTaxCode("15376371009");
		
		Notice notice = new Notice();
		notice.setAmount(1000);
		notice.setPaTaxCode("15376371009");
		List<Notice> notices = new ArrayList<>();
		notices.add(notice);
		
		List<Transfer> transfers = new ArrayList<>();
		transfers.add(transfer);
		notice.setTransfers(transfers);
		body.setNotices(notices);
		
		FeeServiceResponse feeServiceResponse = new FeeServiceResponse();
		feeServiceResponse.setBundleDescription("description");
		feeServiceResponse.setBundleName("name");
		feeServiceResponse.setIdBundle("325643");
		feeServiceResponse.setIdCiBundle("32523");
		feeServiceResponse.setIdPsp("90809792");
		feeServiceResponse.setPaymentMethod(PaymentMethods.BANK_ACCOUNT.toString());
		feeServiceResponse.setPrimaryCiIncurredFee(1000);
		feeServiceResponse.setTaxPayerFee(100);
		feeServiceResponse.setTouchpoint("ATM");
		
		
		List<FeeServiceResponse> listOfFeeServiceResponse = new ArrayList<>();
		listOfFeeServiceResponse.add(feeServiceResponse);
		
		Mockito
		.when(feeService.getFees(Mockito.any(FeeServiceBody.class)))
		.thenReturn(Uni.createFrom().item(listOfFeeServiceResponse));
		
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
	
	@Test
	void testTermsAndConds_500() {
		FeeBody body = new FeeBody();
		body.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.toString());
		
		Transfer transfer = new Transfer();
		transfer.setCategory("KTM");
		transfer.setPaTaxCode("15376371009");
		
		Notice notice = new Notice();
		notice.setAmount(1000);
		notice.setPaTaxCode("15376371009");
		List<Notice> notices = new ArrayList<>();
		notices.add(notice);
		
		List<Transfer> transfers = new ArrayList<>();
		transfers.add(transfer);
		notice.setTransfers(transfers);
		body.setNotices(notices);
		
		Mockito
		.when(feeService.getFees(Mockito.any(FeeServiceBody.class)))
		.thenReturn(Uni.createFrom().failure(new InternalServerErrorException()));
		
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
			
	        Assertions.assertEquals(500, response.statusCode());
	        Assertions.assertEquals("{\"errors\":[\"007000006\"]}", response.getBody().asString());
	     
	}
}
