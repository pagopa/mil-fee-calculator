package it.gov.pagopa.swclient.mil.feecalculator;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import it.gov.pagopa.swclient.mil.feecalculator.bean.GetFeeRequest;
import it.gov.pagopa.swclient.mil.feecalculator.bean.Notice;
import it.gov.pagopa.swclient.mil.feecalculator.bean.PaymentMethods;
import it.gov.pagopa.swclient.mil.feecalculator.bean.Transfer;
import it.gov.pagopa.swclient.mil.feecalculator.client.bean.GecGetFeesResponse;
import it.gov.pagopa.swclient.mil.feecalculator.client.bean.GecGetFeesRequest;
import it.gov.pagopa.swclient.mil.feecalculator.client.FeeService;
import it.gov.pagopa.swclient.mil.feecalculator.dao.PspConfEntity;
import it.gov.pagopa.swclient.mil.feecalculator.dao.PspConfRepository;
import it.gov.pagopa.swclient.mil.feecalculator.dao.PspConfiguration;
import it.gov.pagopa.swclient.mil.feecalculator.resource.FeeCalculatorResource;

@QuarkusTest
@TestHTTPEndpoint(FeeCalculatorResource.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FeeCalculatorResourceTest {
	
	final static String TAX_CODE	= "CHCZLN73D08A662B";
	final static String OUTCOME		= "TERMS_AND_CONDITIONS_NOT_YET_ACCEPTED";
	final static String TOKEN		= "XYZ13243XXYYZZ";
	final static String API_VERSION	= "1.0.0-alpha-a.b-c-somethinglong+build.1-aef.1-its-okay";
	final static String TC_VERSION	= "1";
	
	@InjectMock
	@RestClient
	private FeeService feeService;
	
	@InjectMock
	private PspConfRepository pspConfRepository;
	
	@Test
	void testGetFee_200() {
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
		
		GecGetFeesResponse gecGetFeesResponse = new GecGetFeesResponse();
		gecGetFeesResponse.setBundleDescription("description");
		gecGetFeesResponse.setBundleName("name");
		gecGetFeesResponse.setIdBundle("325643");
		gecGetFeesResponse.setIdCiBundle("32523");
		gecGetFeesResponse.setIdPsp("90809792");
		gecGetFeesResponse.setPaymentMethod(PaymentMethods.BANK_ACCOUNT.toString());
		gecGetFeesResponse.setPrimaryCiIncurredFee(1000);
		gecGetFeesResponse.setTaxPayerFee(100);
		gecGetFeesResponse.setTouchpoint("ATM");
		
		
		List<GecGetFeesResponse> listOfFeeServiceResponse = new ArrayList<>();
		listOfFeeServiceResponse.add(gecGetFeesResponse);
		
		PspConfiguration pspConfiguration = new PspConfiguration();
		pspConfiguration.setPspId("AGID_01");
		pspConfiguration.setPspBroker("9084rt");
		pspConfiguration.setPspPassword("09876yoih");
		PspConfEntity pspConfEntity = new PspConfEntity();
		pspConfEntity.acquirerId = "987654";
		pspConfEntity.pspConfiguration = pspConfiguration;
		
		Mockito
			.when(feeService.getFees(Mockito.any(GecGetFeesRequest.class), Mockito.any(String.class)))
			.thenReturn(Uni.createFrom().item(listOfFeeServiceResponse));
		
		Mockito
			.when(pspConfRepository.findByIdOptional(Mockito.any(String.class)))
			.thenReturn(Uni.createFrom().item(Optional.of(pspConfEntity)));
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", API_VERSION,
						"AcquirerId", "4585625",
						"Channel", "ATM",
						"TerminalId", "0aB9wXyZ")
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
	void testGetFee_500_CommunicationWithGecfailed() {
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
		
		Mockito
			.when(feeService.getFees(Mockito.any(GecGetFeesRequest.class), Mockito.any(String.class)))
			.thenReturn(Uni.createFrom().failure(new InternalServerErrorException()));
		
		Mockito
			.when(pspConfRepository.findByIdOptional(Mockito.any(String.class)))
			.thenReturn(Uni.createFrom().item(Optional.of(pspConfEntity)));
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", API_VERSION,
						"AcquirerId", "4585625",
						"Channel", "ATM",
						"TerminalId", "0aB9wXyZ")
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
		
		GecGetFeesResponse gecGetFeesResponse = new GecGetFeesResponse();
		gecGetFeesResponse.setBundleDescription("description");
		gecGetFeesResponse.setBundleName("name");
		gecGetFeesResponse.setIdBundle("325643");
		gecGetFeesResponse.setIdCiBundle("32523");
		gecGetFeesResponse.setIdPsp("90809792");
		gecGetFeesResponse.setPaymentMethod(PaymentMethods.BANK_ACCOUNT.toString());
		gecGetFeesResponse.setPrimaryCiIncurredFee(1000);
		gecGetFeesResponse.setTaxPayerFee(100);
		gecGetFeesResponse.setTouchpoint("ATM");
		
		List<GecGetFeesResponse> listOfFeeServiceResponse = new ArrayList<>();
		listOfFeeServiceResponse.add(gecGetFeesResponse);
		
		PspConfiguration pspConfiguration = new PspConfiguration();
		pspConfiguration.setPspId("AGID_01");
		pspConfiguration.setPspBroker("9084rt");
		pspConfiguration.setPspPassword("09876yoih");
		PspConfEntity pspConfEntity = new PspConfEntity();
		pspConfEntity.acquirerId = "987654";
		pspConfEntity.pspConfiguration = pspConfiguration;
		
		Mockito
			.when(pspConfRepository.findByIdOptional(Mockito.any(String.class)))
			.thenReturn(Uni.createFrom().item(Optional.empty()));
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", API_VERSION,
						"AcquirerId", "4585625",
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
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.ERROR_RETRIEVING_ID_PSP)); 
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	//Tests mandatory fields
	@Test
	void testGetFee_400_PaymentMethodMandatory() {
		GetFeeRequest body = new GetFeeRequest();
		
		Mockito
			.when(pspConfRepository.findByIdOptional(Mockito.any(String.class)))
			.thenReturn(Uni.createFrom().item(Optional.empty()));
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", API_VERSION,
						"AcquirerId", "4585625",
						"Channel", "ATM",
						"TerminalId", "0aB9wXyZ")
				.and()
				.body(body)
				.when()
				.post()
				.then()
				.extract()
				.response();
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.PAYMENT_METHOD_MUST_NOT_BE_NULL));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_PaymentMethodInvalid() {
		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod("PAY");
		
		Mockito
			.when(pspConfRepository.findByIdOptional(Mockito.any(String.class)))
			.thenReturn(Uni.createFrom().item(Optional.empty()));
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", API_VERSION,
						"AcquirerId", "4585625",
						"Channel", "ATM",
						"TerminalId", "0aB9wXyZ")
				.and()
				.body(body)
				.when()
				.post()
				.then()
				.extract()
				.response();
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.PAYMENT_METHOD_MUST_MATCH_REGEXP));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_NoticeMandatory() {
		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod("PAY");
		
		Mockito
			.when(pspConfRepository.findByIdOptional(Mockito.any(String.class)))
			.thenReturn(Uni.createFrom().item(Optional.empty()));
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", API_VERSION,
						"AcquirerId", "4585625",
						"Channel", "ATM",
						"TerminalId", "0aB9wXyZ")
				.and()
				.body(body)
				.when()
				.post()
				.then()
				.extract()
				.response();
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.NOTICES_MUST_NOT_BE_NULL));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_NoticesExceeded() {
		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod("PAY");
		Notice notice = new Notice();
		
		List<Notice> notices = new ArrayList<>();
		notices.add(notice);
		notices.add(notice);
		notices.add(notice);
		notices.add(notice);
		notices.add(notice);
		notices.add(notice);
		
		body.setNotices(notices);
		
		Mockito
			.when(pspConfRepository.findByIdOptional(Mockito.any(String.class)))
			.thenReturn(Uni.createFrom().item(Optional.empty()));
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", API_VERSION,
						"AcquirerId", "4585625",
						"Channel", "ATM",
						"TerminalId", "0aB9wXyZ")
				.and()
				.body(body)
				.when()
				.post()
				.then()
				.extract()
				.response();
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.NOTICES_LIST_EXCEEDED_SIZE));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_AmountMandatory() {
		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.toString());
		Notice notice = new Notice();
		
		List<Notice> notices = new ArrayList<>();
		notices.add(notice);
		body.setNotices(notices);
		
		Mockito
			.when(pspConfRepository.findByIdOptional(Mockito.any(String.class)))
			.thenReturn(Uni.createFrom().item(Optional.empty()));
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", API_VERSION,
						"AcquirerId", "4585625",
						"Channel", "ATM",
						"TerminalId", "0aB9wXyZ")
				.and()
				.body(body)
				.when()
				.post()
				.then()
				.extract()
				.response();
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.AMOUNT_MUST_NOT_BE_NULL));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_AmountExeedMinSize() {
		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.toString());
		Notice notice = new Notice();
		notice.setAmount(0L);
		
		List<Notice> notices = new ArrayList<>();
		notices.add(notice);
		body.setNotices(notices);
		
		Mockito
			.when(pspConfRepository.findByIdOptional(Mockito.any(String.class)))
			.thenReturn(Uni.createFrom().item(Optional.empty()));
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", API_VERSION,
						"AcquirerId", "4585625",
						"Channel", "ATM",
						"TerminalId", "0aB9wXyZ")
				.and()
				.body(body)
				.when()
				.post()
				.then()
				.extract()
				.response();
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.AMOUNT_EXCEEDED_MIN_VALUE));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_AmountExeedMaxSize() {
		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.toString());
		Notice notice = new Notice();
		notice.setAmount(999999999999L);
		
		List<Notice> notices = new ArrayList<>();
		notices.add(notice);
		body.setNotices(notices);
		
		Mockito
			.when(pspConfRepository.findByIdOptional(Mockito.any(String.class)))
			.thenReturn(Uni.createFrom().item(Optional.empty()));
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", API_VERSION,
						"AcquirerId", "4585625",
						"Channel", "ATM",
						"TerminalId", "0aB9wXyZ")
				.and()
				.body(body)
				.when()
				.post()
				.then()
				.extract()
				.response();
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.AMOUNT_EXCEED_MAX_VALUE));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_PaTaxCodeMandatory() {
		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.toString());
		Notice notice = new Notice();
		notice.setAmount(100L);
		
		List<Notice> notices = new ArrayList<>();
		notices.add(notice);
		body.setNotices(notices);
		
		Mockito
			.when(pspConfRepository.findByIdOptional(Mockito.any(String.class)))
			.thenReturn(Uni.createFrom().item(Optional.empty()));
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", API_VERSION,
						"AcquirerId", "4585625",
						"Channel", "ATM",
						"TerminalId", "0aB9wXyZ")
				.and()
				.body(body)
				.when()
				.post()
				.then()
				.extract()
				.response();
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.PA_TAX_CODE_MUST_NOT_BE_NULL));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_PaTaxCodeNotValid() {
		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.toString());
		Notice notice = new Notice();
		notice.setAmount(100L);
		notice.setPaTaxCode("1111111a111");
		
		List<Notice> notices = new ArrayList<>();
		notices.add(notice);
		body.setNotices(notices);
		
		Mockito
			.when(pspConfRepository.findByIdOptional(Mockito.any(String.class)))
			.thenReturn(Uni.createFrom().item(Optional.empty()));
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", API_VERSION,
						"AcquirerId", "4585625",
						"Channel", "ATM",
						"TerminalId", "0aB9wXyZ")
				.and()
				.body(body)
				.when()
				.post()
				.then()
				.extract()
				.response();
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.PA_TAX_CODE_MUST_MATCH_REGEXP));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_TransfersMandatory() {
		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.toString());
		Notice notice = new Notice();
		notice.setAmount(100L);
		notice.setPaTaxCode("11111112111");
		
		List<Notice> notices = new ArrayList<>();
		notices.add(notice);
		body.setNotices(notices);
		
		Mockito
			.when(pspConfRepository.findByIdOptional(Mockito.any(String.class)))
			.thenReturn(Uni.createFrom().item(Optional.empty()));
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", API_VERSION,
						"AcquirerId", "4585625",
						"Channel", "ATM",
						"TerminalId", "0aB9wXyZ")
				.and()
				.body(body)
				.when()
				.post()
				.then()
				.extract()
				.response();
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.TRANSFERS_MUST_NOT_BE_NULL));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	@Test
	void testGetFee_400_TransfersExeed() {
		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.toString());
		Notice notice = new Notice();
		notice.setAmount(100L);
		notice.setPaTaxCode("11111112111");
		
		List<Transfer> transfers = new ArrayList<>();
		Transfer transfer = new Transfer();
		transfers.add(transfer);
		transfers.add(transfer);
		transfers.add(transfer);
		transfers.add(transfer);
		transfers.add(transfer);
		transfers.add(transfer);
		
		notice.setTransfers(transfers);
		
		List<Notice> notices = new ArrayList<>();
		notices.add(notice);
		body.setNotices(notices);
		
		Mockito
			.when(pspConfRepository.findByIdOptional(Mockito.any(String.class)))
			.thenReturn(Uni.createFrom().item(Optional.empty()));
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", API_VERSION,
						"AcquirerId", "4585625",
						"Channel", "ATM",
						"TerminalId", "0aB9wXyZ")
				.and()
				.body(body)
				.when()
				.post()
				.then()
				.extract()
				.response();
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.TRANSFERS_EXCEEDED_MAX_VALUE));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}

	@Test
	void testGetFee_400_TransfersPaTaxCodeMandatory() {
		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.toString());
		Notice notice = new Notice();
		notice.setAmount(100L);
		notice.setPaTaxCode("11111111111");
		
		List<Transfer> transfers = new ArrayList<>();
		Transfer transfer = new Transfer();
		transfers.add(transfer);
		
		notice.setTransfers(transfers);
		
		List<Notice> notices = new ArrayList<>();
		notices.add(notice);
		body.setNotices(notices);
		
		Mockito
			.when(pspConfRepository.findByIdOptional(Mockito.any(String.class)))
			.thenReturn(Uni.createFrom().item(Optional.empty()));
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", API_VERSION,
						"AcquirerId", "4585625",
						"Channel", "ATM",
						"TerminalId", "0aB9wXyZ")
				.and()
				.body(body)
				.when()
				.post()
				.then()
				.extract()
				.response();
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.TRANSFERS_PA_TAX_CODE_MUST_NOT_BE_NULL));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_TransfersPaTaxCodeInvalid() {
		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.toString());
		Notice notice = new Notice();
		notice.setAmount(100L);
		notice.setPaTaxCode("11111112111");
		
		List<Transfer> transfers = new ArrayList<>();
		Transfer transfer = new Transfer();
		transfer.setPaTaxCode("1111111a111");
		transfers.add(transfer);
		
		notice.setTransfers(transfers);
		
		List<Notice> notices = new ArrayList<>();
		notices.add(notice);
		body.setNotices(notices);
		
		Mockito
			.when(pspConfRepository.findByIdOptional(Mockito.any(String.class)))
			.thenReturn(Uni.createFrom().item(Optional.empty()));
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", API_VERSION,
						"AcquirerId", "4585625",
						"Channel", "ATM",
						"TerminalId", "0aB9wXyZ")
				.and()
				.body(body)
				.when()
				.post()
				.then()
				.extract()
				.response();
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.TRANSFERS_PA_TAX_CODE_MUST_MATCH_REGEXP));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_TransfersCategoryMandatory() {
		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.toString());
		Notice notice = new Notice();
		notice.setAmount(100L);
		notice.setPaTaxCode("11111112111");
		
		List<Transfer> transfers = new ArrayList<>();
		Transfer transfer = new Transfer();
		transfer.setPaTaxCode("11111112111");
		transfers.add(transfer);
		
		notice.setTransfers(transfers);
		
		List<Notice> notices = new ArrayList<>();
		notices.add(notice);
		body.setNotices(notices);
		
		Mockito
			.when(pspConfRepository.findByIdOptional(Mockito.any(String.class)))
			.thenReturn(Uni.createFrom().item(Optional.empty()));
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", API_VERSION,
						"AcquirerId", "4585625",
						"Channel", "ATM",
						"TerminalId", "0aB9wXyZ")
				.and()
				.body(body)
				.when()
				.post()
				.then()
				.extract()
				.response();
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.TRANSFERS_CATEGORY_MUST_NOT_BE_NULL));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_TransfersCategoryInvalid() {
		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.toString());
		Notice notice = new Notice();
		notice.setAmount(100L);
		notice.setPaTaxCode("11111112111");
		
		List<Transfer> transfers = new ArrayList<>();
		Transfer transfer = new Transfer();
		transfer.setPaTaxCode("11111112111");
		transfer.setCategory("d234Z°3er");
		transfers.add(transfer);
		
		notice.setTransfers(transfers);
		
		List<Notice> notices = new ArrayList<>();
		notices.add(notice);
		body.setNotices(notices);
		
		Mockito
			.when(pspConfRepository.findByIdOptional(Mockito.any(String.class)))
			.thenReturn(Uni.createFrom().item(Optional.empty()));
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", API_VERSION,
						"AcquirerId", "4585625",
						"Channel", "ATM",
						"TerminalId", "0aB9wXyZ")
				.and()
				.body(body)
				.when()
				.post()
				.then()
				.extract()
				.response();
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.TRANSFERS_CATEGORY_MUST_MATCH_REGEXP));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	//TEST headers fields
	@Test
	void testGetFee_400_RequestIdMandatory() {
		GetFeeRequest bodyRequest = new GetFeeRequest();
		bodyRequest.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.toString());
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"Version", API_VERSION,
						"AcquirerId", "4585625",
						"Channel", "ATM",
						"TerminalId", "0aB9wXyZ")
				.and()
				.body(bodyRequest)
				.when()
				.post()
				.then()
				.extract()
				.response();
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(it.gov.pagopa.swclient.mil.ErrorCode.REQUEST_ID_MUST_NOT_BE_NULL ));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_RequestIdNotValid() {
		GetFeeRequest bodyRequest = new GetFeeRequest();
		bodyRequest.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.toString());
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b56899fedccb642b",
						"Version", API_VERSION,
						"AcquirerId", "4585625",
						"Channel", "ATM",
						"TerminalId", "0aB9wXyZ")
				.and()
				.body(bodyRequest)
				.when()
				.post()
				.then()
				.extract()
				.response();
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(it.gov.pagopa.swclient.mil.ErrorCode.REQUEST_ID_MUST_MATCH_REGEXP ));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_VersionNotValid() {
		GetFeeRequest bodyRequest = new GetFeeRequest();
		bodyRequest.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.toString());
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b56899fedccb642b",
						"Version", "1,0",
						"AcquirerId", "4585625",
						"Channel", "ATM",
						"TerminalId", "0aB9wXyZ")
				.and()
				.body(bodyRequest)
				.when()
				.post()
				.then()
				.extract()
				.response();
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(it.gov.pagopa.swclient.mil.ErrorCode.VERSION_MUST_MATCH_REGEXP ));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_VersionExceed() {
		GetFeeRequest bodyRequest = new GetFeeRequest();
		bodyRequest.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.toString());
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b56899fedccb642b",
						"Version", "1.0.0-alpha-a.b-c-somethinglong+build.1-aef.1-its-okayokayokayokay",
						"AcquirerId", "4585625",
						"Channel", "ATM",
						"TerminalId", "0aB9wXyZ")
				.and()
				.body(bodyRequest)
				.when()
				.post()
				.then()
				.extract()
				.response();
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(it.gov.pagopa.swclient.mil.ErrorCode.VERSION_SIZE_MUST_BE_AT_MOST_MAX ));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_AcquirerIdMandatory() {
		GetFeeRequest bodyRequest = new GetFeeRequest();
		bodyRequest.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.toString());
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", API_VERSION,
						"Channel", "ATM",
						"TerminalId", "0aB9wXyZ")
				.and()
				.body(bodyRequest)
				.when()
				.post()
				.then()
				.extract()
				.response();
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(it.gov.pagopa.swclient.mil.ErrorCode.ACQUIRER_ID_MUST_NOT_BE_NULL ));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_AcquirerIdNotValid() {
		GetFeeRequest bodyRequest = new GetFeeRequest();
		bodyRequest.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.toString());
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", API_VERSION,
						"AcquirerId", "45856a25",
						"Channel", "ATM",
						"TerminalId", "0aB9wXyZ")
				.and()
				.body(bodyRequest)
				.when()
				.post()
				.then()
				.extract()
				.response();
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(it.gov.pagopa.swclient.mil.ErrorCode.ACQUIRER_ID_MUST_MATCH_REGEXP ));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_ChannelMandatory() {
		GetFeeRequest bodyRequest = new GetFeeRequest();
		bodyRequest.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.toString());
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", API_VERSION,
						"AcquirerId", "45856a25",
						"TerminalId", "0aB9wXyZ")
				.and()
				.body(bodyRequest)
				.when()
				.post()
				.then()
				.extract()
				.response();
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(it.gov.pagopa.swclient.mil.ErrorCode.CHANNEL_MUST_NOT_BE_NULL ));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_ChannelNotValid() {
		GetFeeRequest bodyRequest = new GetFeeRequest();
		bodyRequest.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.toString());
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", API_VERSION,
						"AcquirerId", "45856a25",
						"Channel", "NOT VALID",
						"TerminalId", "0aB9wXyZ")
				.and()
				.body(bodyRequest)
				.when()
				.post()
				.then()
				.extract()
				.response();
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(it.gov.pagopa.swclient.mil.ErrorCode.CHANNEL_MUST_MATCH_REGEXP ));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_TerminalIdMandatory() {
		GetFeeRequest bodyRequest = new GetFeeRequest();
		bodyRequest.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.toString());
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", API_VERSION,
						"AcquirerId", "45856a25",
						"Channel", "ATM")
				.and()
				.body(bodyRequest)
				.when()
				.post()
				.then()
				.extract()
				.response();
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(it.gov.pagopa.swclient.mil.ErrorCode.TERMINAL_ID_MUST_NOT_BE_NULL ));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	@Test
	void testGetFee_400_TerminalIdNotValid() {
		GetFeeRequest bodyRequest = new GetFeeRequest();
		bodyRequest.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.toString());
		
		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", "d0d654e6-97da-4848-b568-99fedccb642b",
						"Version", API_VERSION,
						"AcquirerId", "45856a25",
						"Channel", "ATM",
						"TerminalId", "0aB9wXyZ9987868765h")
				.and()
				.body(bodyRequest)
				.when()
				.post()
				.then()
				.extract()
				.response();
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(it.gov.pagopa.swclient.mil.ErrorCode.TERMINAL_ID_MUST_MATCH_REGEXP ));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
}
