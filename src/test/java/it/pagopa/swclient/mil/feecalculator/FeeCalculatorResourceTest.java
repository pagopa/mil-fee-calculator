package it.pagopa.swclient.mil.feecalculator;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.smallrye.mutiny.Uni;
import it.pagopa.swclient.mil.feecalculator.bean.GetFeeRequest;
import it.pagopa.swclient.mil.feecalculator.bean.Notice;
import it.pagopa.swclient.mil.feecalculator.bean.PaymentMethods;
import it.pagopa.swclient.mil.feecalculator.bean.Transfer;
import it.pagopa.swclient.mil.feecalculator.client.FeeService;
import it.pagopa.swclient.mil.feecalculator.client.MilRestService;
import it.pagopa.swclient.mil.feecalculator.client.bean.AcquirerConfiguration;
import it.pagopa.swclient.mil.feecalculator.client.bean.BundleOption;
import it.pagopa.swclient.mil.feecalculator.client.bean.GecGetFeesRequest;
import it.pagopa.swclient.mil.feecalculator.client.bean.GecGetFeesResponse;
import it.pagopa.swclient.mil.feecalculator.client.bean.PspConfiguration;
import it.pagopa.swclient.mil.feecalculator.resource.FeeCalculatorResource;

@QuarkusTest
@TestHTTPEndpoint(FeeCalculatorResource.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FeeCalculatorResourceTest {

	final static String API_VERSION	= "1.0.0";

	@InjectMock
	@RestClient
	FeeService feeService;
	
	@InjectMock
	@RestClient
	MilRestService milRestService;

	Map<String, String> commonHeaders;

	AcquirerConfiguration acquirerConfiguration;

	GecGetFeesResponse gecGetFeesResponse;

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

		// acquirer PSP configuration
		acquirerConfiguration = new AcquirerConfiguration();

		PspConfiguration pspConfiguration = new PspConfiguration();
		pspConfiguration.setPsp("AGID_01");
		pspConfiguration.setBroker("97735020584");
		pspConfiguration.setChannel("97735020584_07");
		pspConfiguration.setPassword("PLACEHOLDER");

		acquirerConfiguration.setPspConfigForVerifyAndActivate(pspConfiguration);
		acquirerConfiguration.setPspConfigForGetFeeAndClosePayment(pspConfiguration);

		// GEC getFees response
		gecGetFeesResponse = new GecGetFeesResponse();

		gecGetFeesResponse.setBelowThreshold(false);

		BundleOption bundleOption1 = new BundleOption();
		bundleOption1.setTaxPayerFee(50);
		bundleOption1.setPrimaryCiIncurredFee(0);
		bundleOption1.setPaymentMethod("ANY");
		bundleOption1.setTouchpoint("ANY");
		bundleOption1.setIdBundle("b51f2062-def7-4976-a258-6c94f57d402a");
		bundleOption1.setBundleName("soft-cli-test10");
		bundleOption1.setBundleDescription("pacchetto di test per software client");
		bundleOption1.setIdCiBundle(null);
		bundleOption1.setIdPsp("AGID_01");
		bundleOption1.setIdChannel(null);
		bundleOption1.setIdBrokerPsp(null);
		bundleOption1.setOnUs(false);
		bundleOption1.setAbi(null);

		BundleOption bundleOption2 = new BundleOption();
		bundleOption2.setTaxPayerFee(100);
		bundleOption2.setPrimaryCiIncurredFee(0);
		bundleOption2.setPaymentMethod("ANY");
		bundleOption2.setTouchpoint("ANY");
		bundleOption2.setIdBundle("5afb70a5-b427-49a3-8e2c-4b74625e4fe1");
		bundleOption2.setBundleName("soft-cli-test7");
		bundleOption2.setBundleDescription("pacchetto di test per software client");
		bundleOption2.setIdCiBundle(null);
		bundleOption2.setIdPsp("AGID_01");
		bundleOption2.setIdChannel(null);
		bundleOption2.setIdBrokerPsp(null);
		bundleOption2.setOnUs(false);
		bundleOption2.setAbi(null);

		BundleOption bundleOption3 = new BundleOption();
		bundleOption3.setTaxPayerFee(100);
		bundleOption3.setPrimaryCiIncurredFee(0);
		bundleOption3.setPaymentMethod("ANY");
		bundleOption3.setTouchpoint("ANY");
		bundleOption3.setIdBundle("ca9a56e7-aa02-4d11-a8c8-71e1670f974e");
		bundleOption3.setBundleName("soft-cli-test8");
		bundleOption3.setBundleDescription("pacchetto di test per software client");
		bundleOption3.setIdCiBundle(null);
		bundleOption3.setIdPsp("AGID_01");
		bundleOption3.setIdChannel(null);
		bundleOption3.setIdBrokerPsp(null);
		bundleOption3.setOnUs(false);
		bundleOption3.setAbi(null);

		BundleOption bundleOption4 = new BundleOption();
		bundleOption4.setTaxPayerFee(100);
		bundleOption4.setPrimaryCiIncurredFee(0);
		bundleOption4.setPaymentMethod("ANY");
		bundleOption4.setTouchpoint("ANY");
		bundleOption4.setIdBundle("683589f9-9471-4bd4-ba44-a1044aea4dcc");
		bundleOption4.setBundleName("soft-cli-test9");
		bundleOption4.setBundleDescription("pacchetto di test per software client");
		bundleOption4.setIdCiBundle(null);
		bundleOption4.setIdPsp("AGID_01");
		bundleOption4.setIdChannel(null);
		bundleOption4.setIdBrokerPsp(null);
		bundleOption4.setOnUs(false);
		bundleOption4.setAbi(null);

		gecGetFeesResponse.setBundleOptions(Arrays.asList(bundleOption1, bundleOption2, bundleOption3, bundleOption4));

	}

	@Test
	void testGetFee_200() {

		Transfer transfer1 = new Transfer();
		transfer1.setCategory("");
		transfer1.setPaTaxCode("15376371009");

		Transfer transfer2 = new Transfer();
		transfer2.setCategory("TEST_CATEGORY");
		transfer2.setPaTaxCode("15376371010");
		
		Notice notice = new Notice();
		notice.setAmount(1000L);
		notice.setPaTaxCode("15376371009");
		notice.setTransfers(Arrays.asList(transfer1, transfer2));

		GetFeeRequest getFeeRequest = new GetFeeRequest();
		getFeeRequest.setPaymentMethod(PaymentMethods.PAYMENT_CARD.name());
		getFeeRequest.setNotices(List.of(notice));

		Mockito
				.when(milRestService.getPspConfiguration(Mockito.any(String.class), Mockito.any(String.class)))
				.thenReturn(Uni.createFrom().item(acquirerConfiguration));

		Mockito
				.when(feeService.getFees(Mockito.any(String.class), Mockito.any(GecGetFeesRequest.class)))
				.thenReturn(Uni.createFrom().item(gecGetFeesResponse));

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
        Assertions.assertNotNull(response.jsonPath().getJsonObject("fee"));
		Assertions.assertEquals(
				gecGetFeesResponse.getBundleOptions().get(0).getTaxPayerFee(),
				response.jsonPath().getLong("fee"));

		// verify request to the mil rest client
		ArgumentCaptor<String> requestIdCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> acquirerIdCaptor = ArgumentCaptor.forClass(String.class);

		Mockito.verify(milRestService).getPspConfiguration(requestIdCaptor.capture(), acquirerIdCaptor.capture());

		Assertions.assertEquals(commonHeaders.get("RequestId"), requestIdCaptor.getValue());
		Assertions.assertEquals(commonHeaders.get("AcquirerId"), acquirerIdCaptor.getValue());

		// verify request to GEC
		ArgumentCaptor<String> gecRequestIdCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<GecGetFeesRequest> gecRequestCaptor = ArgumentCaptor.forClass(GecGetFeesRequest.class);

		Mockito.verify(feeService).getFees(gecRequestIdCaptor.capture(), gecRequestCaptor.capture());

		Assertions.assertEquals(commonHeaders.get("RequestId"), gecRequestIdCaptor.getValue());
		Assertions.assertEquals(
				acquirerConfiguration.getPspConfigForGetFeeAndClosePayment().getPsp(),
				gecRequestCaptor.getValue().getIdPspList().get(0).getIdPsp()
		);
		Assertions.assertEquals(notice.getAmount(), gecRequestCaptor.getValue().getPaymentAmount());
		Assertions.assertEquals(notice.getPaTaxCode(), gecRequestCaptor.getValue().getPrimaryCreditorInstitution());
		Assertions.assertEquals("CP", gecRequestCaptor.getValue().getPaymentMethod());
		Assertions.assertEquals(commonHeaders.get("Channel"), gecRequestCaptor.getValue().getTouchpoint());
		int i = 0;
		for (Transfer transfer: notice.getTransfers()) {
			Assertions.assertEquals(transfer.getPaTaxCode(), gecRequestCaptor.getValue().getTransferList().get(i).getCreditorInstitution());
			Assertions.assertEquals(
					StringUtils.equals(transfer.getCategory(), StringUtils.EMPTY) ? null : transfer.getCategory(),
					gecRequestCaptor.getValue().getTransferList().get(i).getTransferCategory()
			);
			i++;
		}

	}

	@ParameterizedTest
	@CsvFileSource(resources = "/payment_method_mapping.csv", numLinesToSkip = 1)
	void testGetFee_200_paymentMethodMapping(String milPaymentMethod, String gecPaymentMethod) {

		Transfer transfer1 = new Transfer();
		transfer1.setCategory("");
		transfer1.setPaTaxCode("15376371009");

		Transfer transfer2 = new Transfer();
		transfer2.setCategory("TEST_CATEGORY");
		transfer2.setPaTaxCode("15376371010");

		Notice notice = new Notice();
		notice.setAmount(1000L);
		notice.setPaTaxCode("15376371009");
		notice.setTransfers(Arrays.asList(transfer1, transfer2));

		GetFeeRequest getFeeRequest = new GetFeeRequest();
		getFeeRequest.setPaymentMethod(milPaymentMethod);
		getFeeRequest.setNotices(List.of(notice));

		Mockito
				.when(milRestService.getPspConfiguration(Mockito.any(String.class), Mockito.any(String.class)))
				.thenReturn(Uni.createFrom().item(acquirerConfiguration));

		Mockito
				.when(feeService.getFees(Mockito.any(String.class), Mockito.any(GecGetFeesRequest.class)))
				.thenReturn(Uni.createFrom().item(gecGetFeesResponse));

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
		Assertions.assertNotNull(response.jsonPath().getJsonObject("fee"));

		// verify request to GEC
		ArgumentCaptor<String> gecRequestIdCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<GecGetFeesRequest> gecRequestCaptor = ArgumentCaptor.forClass(GecGetFeesRequest.class);

		Mockito.verify(feeService).getFees(gecRequestIdCaptor.capture(), gecRequestCaptor.capture());
		Assertions.assertEquals(gecPaymentMethod, gecRequestCaptor.getValue().getPaymentMethod());
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/touchpoint_mapping.csv", numLinesToSkip = 1)
	void testGetFee_200_touchpointMapping(String milTouchpoint, String gecTouchpoint) {

		Transfer transfer1 = new Transfer();
		transfer1.setCategory("");
		transfer1.setPaTaxCode("15376371009");

		Transfer transfer2 = new Transfer();
		transfer2.setCategory("TEST_CATEGORY");
		transfer2.setPaTaxCode("15376371010");

		Notice notice = new Notice();
		notice.setAmount(1000L);
		notice.setPaTaxCode("15376371009");
		notice.setTransfers(Arrays.asList(transfer1, transfer2));

		GetFeeRequest getFeeRequest = new GetFeeRequest();
		getFeeRequest.setPaymentMethod(PaymentMethods.PAYMENT_CARD.name());
		getFeeRequest.setNotices(List.of(notice));

		Mockito
				.when(milRestService.getPspConfiguration(Mockito.any(String.class), Mockito.any(String.class)))
				.thenReturn(Uni.createFrom().item(acquirerConfiguration));

		Mockito
				.when(feeService.getFees(Mockito.any(String.class), Mockito.any(GecGetFeesRequest.class)))
				.thenReturn(Uni.createFrom().item(gecGetFeesResponse));

		Response response = given()
				.contentType(ContentType.JSON)
				.headers(
						"RequestId", UUID.randomUUID().toString(),
						"Version", API_VERSION,
						"AcquirerId", "4585625",
						"Channel", milTouchpoint,
						"TerminalId", "0aB9wXyZ",
						"SessionId", UUID.randomUUID().toString(),
						"MerchantId", "28405fHfk73x88D")
				.and()
				.body(getFeeRequest)
				.when()
				.post()
				.then()
				.extract()
				.response();

		Assertions.assertEquals(200, response.statusCode());
		Assertions.assertNotNull(response.jsonPath().getJsonObject("fee"));

		// verify request to GEC
		ArgumentCaptor<String> gecRequestIdCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<GecGetFeesRequest> gecRequestCaptor = ArgumentCaptor.forClass(GecGetFeesRequest.class);

		Mockito.verify(feeService).getFees(gecRequestIdCaptor.capture(), gecRequestCaptor.capture());
		Assertions.assertEquals(gecTouchpoint, gecRequestCaptor.getValue().getTouchpoint());
	}

	@Test
	void testGetFee_404_gecEmptyBundleList() {

		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod(PaymentMethods.PAYMENT_CARD.toString());

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

		GecGetFeesResponse gecEmptyResponse = new GecGetFeesResponse();
		gecEmptyResponse.setBelowThreshold(false);
		gecEmptyResponse.setBundleOptions(new ArrayList<>());

		Mockito
				.when(milRestService.getPspConfiguration(Mockito.any(String.class), Mockito.any(String.class)))
				.thenReturn(Uni.createFrom().item(acquirerConfiguration));

		Mockito
				.when(feeService.getFees(Mockito.any(String.class), Mockito.any(GecGetFeesRequest.class)))
				.thenReturn(Uni.createFrom().item(gecEmptyResponse));

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
		Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.NO_FEE_FOUND));
		Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_500_gecError() {
		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod(PaymentMethods.PAYMENT_CARD.toString());
		
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

		Mockito
			.when(feeService.getFees(Mockito.any(String.class), Mockito.any(GecGetFeesRequest.class)))
			.thenReturn(Uni.createFrom().failure(new ClientWebApplicationException(404)));

		Mockito
				.when(milRestService.getPspConfiguration(Mockito.any(String.class), Mockito.any(String.class)))
				.thenReturn(Uni.createFrom().item(acquirerConfiguration));
		
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
		bodyRequest.setPaymentMethod(PaymentMethods.PAYMENT_CARD.name());

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

		Mockito
				.when(milRestService.getPspConfiguration(Mockito.any(String.class), Mockito.any(String.class)))
				.thenReturn(Uni.createFrom().failure(new ClientWebApplicationException(404)));
		
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
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.UNKNOWN_ACQUIRER_ID));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}

	@Test
	void testGetFee_500_milTimeout() {

		GetFeeRequest bodyRequest = new GetFeeRequest();
		bodyRequest.setPaymentMethod(PaymentMethods.PAYMENT_CARD.name());

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

		Mockito
				.when(milRestService.getPspConfiguration(Mockito.any(String.class), Mockito.any(String.class)))
				.thenReturn(Uni.createFrom().failure(new TimeoutException()));

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
		Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.ERROR_CALLING_MIL_REST_SERVICES));
		Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	//Tests mandatory fields
	@Test
	void testGetFee_400_EmptyRequest() {

		Response response = given()
				.contentType(ContentType.JSON)
				.headers(commonHeaders)
				.when()
				.post()
				.then()
				.extract()
				.response();

		Assertions.assertEquals(400, response.statusCode());
		Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.REQUEST_MUST_NOT_BE_EMPTY));
		Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	@Test
	void testGetFee_400_PaymentMethodMandatory() {

		GetFeeRequest body = new GetFeeRequest();

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
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.PAYMENT_METHOD_MUST_NOT_BE_NULL));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_PaymentMethodInvalid() {

		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod("PAY");

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
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.PAYMENT_METHOD_MUST_MATCH_REGEXP));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_NoticeMandatory() {

		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.name());

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
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.NOTICES_MUST_NOT_BE_NULL));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_NoticesExceeded() {

		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.name());
		Notice notice = new Notice();
		notice.setAmount(12345L);
		notice.setPaTaxCode("15376371009");
		Transfer transfer = new Transfer();
		transfer.setCategory("KTM");
		transfer.setPaTaxCode("15376371009");
		notice.setTransfers(List.of(transfer));
		
		List<Notice> notices = new ArrayList<>();
		notices.add(notice);
		notices.add(notice);
		notices.add(notice);
		notices.add(notice);
		notices.add(notice);
		notices.add(notice);
		
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
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.NOTICES_LIST_EXCEEDED_SIZE));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_AmountMandatory() {

		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.name());
		Notice notice = new Notice();
		
		List<Notice> notices = new ArrayList<>();
		notices.add(notice);
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
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.AMOUNT_MUST_NOT_BE_NULL));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_AmountLessThanMin() {
		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.name());
		Notice notice = new Notice();
		notice.setAmount(0L);
		
		List<Notice> notices = new ArrayList<>();
		notices.add(notice);
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
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.AMOUNT_EXCEEDED_MIN_VALUE));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_AmountGreaterThanMax() {

		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.name());
		Notice notice = new Notice();
		notice.setAmount(999999999999L);
		
		List<Notice> notices = new ArrayList<>();
		notices.add(notice);
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
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.AMOUNT_EXCEED_MAX_VALUE));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_PaTaxCodeMandatory() {

		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.name());
		Notice notice = new Notice();
		notice.setAmount(100L);
		
		List<Notice> notices = new ArrayList<>();
		notices.add(notice);
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
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.PA_TAX_CODE_MUST_NOT_BE_NULL));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_PaTaxCodeNotValid() {

		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.name());
		Notice notice = new Notice();
		notice.setAmount(100L);
		notice.setPaTaxCode("1111111a111");
		
		List<Notice> notices = new ArrayList<>();
		notices.add(notice);
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
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.PA_TAX_CODE_MUST_MATCH_REGEXP));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_TransfersMandatory() {

		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.name());
		Notice notice = new Notice();
		notice.setAmount(100L);
		notice.setPaTaxCode("11111112111");
		
		List<Notice> notices = new ArrayList<>();
		notices.add(notice);
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
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.TRANSFERS_MUST_NOT_BE_NULL));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	@Test
	void testGetFee_400_TransfersExceeded() {

		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.name());
		Notice notice = new Notice();
		notice.setAmount(100L);
		notice.setPaTaxCode("15376371009");
		
		Transfer transfer = new Transfer();
		transfer.setCategory("KTM");
		transfer.setPaTaxCode("15376371009");
		notice.setTransfers(List.of(transfer));
		
		List<Transfer> transfers = new ArrayList<>();
		
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
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.TRANSFERS_EXCEEDED_MAX_VALUE));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}

	@Test
	void testGetFee_400_TransfersPaTaxCodeMandatory() {

		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.name());
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
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.TRANSFERS_PA_TAX_CODE_MUST_NOT_BE_NULL));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_TransfersPaTaxCodeInvalid() {

		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.name());
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
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.TRANSFERS_PA_TAX_CODE_MUST_MATCH_REGEXP));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_TransfersCategoryMandatory() {

		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.name());
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
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.TRANSFERS_CATEGORY_MUST_NOT_BE_NULL));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_TransfersCategoryInvalid() {
		GetFeeRequest body = new GetFeeRequest();
		body.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.name());
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
			
        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.TRANSFERS_CATEGORY_MUST_MATCH_REGEXP));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	//TEST headers fields
	@Test
	void testGetFee_400_RequestIdMandatory() {

		GetFeeRequest bodyRequest = new GetFeeRequest();
		bodyRequest.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.name());
		
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
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(it.pagopa.swclient.mil.ErrorCode.REQUEST_ID_MUST_NOT_BE_NULL ));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_RequestIdNotValid() {

		GetFeeRequest bodyRequest = new GetFeeRequest();
		bodyRequest.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.name());
		
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
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(it.pagopa.swclient.mil.ErrorCode.REQUEST_ID_MUST_MATCH_REGEXP ));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_VersionNotValid() {

		GetFeeRequest bodyRequest = new GetFeeRequest();
		bodyRequest.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.name());
		
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
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(it.pagopa.swclient.mil.ErrorCode.VERSION_MUST_MATCH_REGEXP ));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_VersionExceed() {

		GetFeeRequest bodyRequest = new GetFeeRequest();
		bodyRequest.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.name());
		
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
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(it.pagopa.swclient.mil.ErrorCode.VERSION_SIZE_MUST_BE_AT_MOST_MAX ));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_AcquirerIdMandatory() {
		GetFeeRequest bodyRequest = new GetFeeRequest();
		bodyRequest.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.name());
		
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
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(it.pagopa.swclient.mil.ErrorCode.ACQUIRER_ID_MUST_NOT_BE_NULL ));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_AcquirerIdNotValid() {
		GetFeeRequest bodyRequest = new GetFeeRequest();
		bodyRequest.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.name());
		
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
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(it.pagopa.swclient.mil.ErrorCode.ACQUIRER_ID_MUST_MATCH_REGEXP ));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_ChannelMandatory() {
		GetFeeRequest bodyRequest = new GetFeeRequest();
		bodyRequest.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.name());
		
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
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(it.pagopa.swclient.mil.ErrorCode.CHANNEL_MUST_NOT_BE_NULL ));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_400_ChannelNotValid() {
		GetFeeRequest bodyRequest = new GetFeeRequest();
		bodyRequest.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.name());
		
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
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(it.pagopa.swclient.mil.ErrorCode.CHANNEL_MUST_MATCH_REGEXP ));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
	
	@Test
	void testGetFee_TerminalIdMandatory() {
		GetFeeRequest bodyRequest = new GetFeeRequest();
		bodyRequest.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.name());
		
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
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(it.pagopa.swclient.mil.ErrorCode.TERMINAL_ID_MUST_NOT_BE_NULL ));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}

	@Test
	void testGetFee_400_TerminalIdNotValid() {
		GetFeeRequest bodyRequest = new GetFeeRequest();
		bodyRequest.setPaymentMethod(PaymentMethods.PAGOBANCOMAT.name());
		
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
        Assertions.assertTrue(response.jsonPath().getList("errors").contains(it.pagopa.swclient.mil.ErrorCode.TERMINAL_ID_MUST_MATCH_REGEXP ));
        Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
}
