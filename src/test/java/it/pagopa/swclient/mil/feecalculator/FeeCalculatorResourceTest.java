package it.pagopa.swclient.mil.feecalculator;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.smallrye.mutiny.Uni;
import it.pagopa.swclient.mil.feecalculator.bean.GetFeeRequest;
import it.pagopa.swclient.mil.feecalculator.bean.Transfer;
import it.pagopa.swclient.mil.feecalculator.client.FeeService;
import it.pagopa.swclient.mil.feecalculator.client.MilRestService;
import it.pagopa.swclient.mil.feecalculator.client.bean.AcquirerConfiguration;
import it.pagopa.swclient.mil.feecalculator.client.bean.BundleOption;
import it.pagopa.swclient.mil.feecalculator.client.bean.GecGetFeesRequest;
import it.pagopa.swclient.mil.feecalculator.client.bean.GecGetFeesResponse;
import it.pagopa.swclient.mil.feecalculator.resource.FeeCalculatorResource;
import it.pagopa.swclient.mil.feecalculator.util.FeeCalculatorTestData;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(FeeCalculatorResource.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FeeCalculatorResourceTest {

	@InjectMock
	@RestClient
	FeeService feeService;
	
	@InjectMock
	MilRestService milRestService;

	Map<String, String> milHeaders;

	AcquirerConfiguration acquirerConfiguration;

	GecGetFeesResponse gecGetFeesResponse;

	@BeforeAll
	void createTestObjects() {

		// common headers
		milHeaders = FeeCalculatorTestData.getMilHeaders(true, true);

		// acquirer PSP configuration
		acquirerConfiguration = FeeCalculatorTestData.getAcquirerConfiguration();

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
	@TestSecurity(user = "testUser", roles = { "NoticePayer" })
	void testGetFee_200() {

		GetFeeRequest getFeeRequest = FeeCalculatorTestData.getFeeRequest();

		Mockito
				.when(milRestService.getPspConfiguration(Mockito.any(String.class)))
				.thenReturn(Uni.createFrom().item(acquirerConfiguration));

		Mockito
				.when(feeService.getFees(Mockito.any(String.class), Mockito.any(GecGetFeesRequest.class)))
				.thenReturn(Uni.createFrom().item(gecGetFeesResponse));

		Response response = given()
				.contentType(ContentType.JSON)
				.headers(milHeaders)
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
		ArgumentCaptor<String> acquirerIdCaptor = ArgumentCaptor.forClass(String.class);

		Mockito.verify(milRestService).getPspConfiguration(acquirerIdCaptor.capture());

		Assertions.assertEquals(milHeaders.get("AcquirerId"), acquirerIdCaptor.getValue());

		// verify request to GEC
		ArgumentCaptor<String> gecRequestIdCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<GecGetFeesRequest> gecRequestCaptor = ArgumentCaptor.forClass(GecGetFeesRequest.class);

		Mockito.verify(feeService).getFees(gecRequestIdCaptor.capture(), gecRequestCaptor.capture());

		Assertions.assertEquals(milHeaders.get("RequestId"), gecRequestIdCaptor.getValue());
		Assertions.assertEquals(
				acquirerConfiguration.getPspConfigForGetFeeAndClosePayment().getPsp(),
				gecRequestCaptor.getValue().getIdPspList().get(0).getIdPsp()
		);
		Assertions.assertEquals(getFeeRequest.getNotices().get(0).getAmount(), gecRequestCaptor.getValue().getPaymentAmount());
		Assertions.assertEquals(getFeeRequest.getNotices().get(0).getPaTaxCode(), gecRequestCaptor.getValue().getPrimaryCreditorInstitution());
		Assertions.assertEquals("CP", gecRequestCaptor.getValue().getPaymentMethod());
		Assertions.assertEquals(milHeaders.get("Channel"), gecRequestCaptor.getValue().getTouchpoint());
		int i = 0;
		for (Transfer transfer: getFeeRequest.getNotices().get(0).getTransfers()) {
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
	@TestSecurity(user = "testUser", roles = { "NoticePayer" })
	void testGetFee_200_paymentMethodMapping(String milPaymentMethod, String gecPaymentMethod) {

		GetFeeRequest getFeeRequest = FeeCalculatorTestData.getFeeRequest();
		getFeeRequest.setPaymentMethod(milPaymentMethod);

		Mockito
				.when(milRestService.getPspConfiguration(Mockito.any(String.class)))
				.thenReturn(Uni.createFrom().item(acquirerConfiguration));

		Mockito
				.when(feeService.getFees(Mockito.any(String.class), Mockito.any(GecGetFeesRequest.class)))
				.thenReturn(Uni.createFrom().item(gecGetFeesResponse));

		Response response = given()
				.contentType(ContentType.JSON)
				.headers(milHeaders)
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
	@TestSecurity(user = "testUser", roles = { "NoticePayer" })
	void testGetFee_200_touchpointMapping(String milTouchpoint, String gecTouchpoint) {

		Map<String, String> headers = FeeCalculatorTestData.getMilHeaders(true, true);
		headers.put("Channel", milTouchpoint);

		Mockito
				.when(milRestService.getPspConfiguration(Mockito.any(String.class)))
				.thenReturn(Uni.createFrom().item(acquirerConfiguration));

		Mockito
				.when(feeService.getFees(Mockito.any(String.class), Mockito.any(GecGetFeesRequest.class)))
				.thenReturn(Uni.createFrom().item(gecGetFeesResponse));

		Response response = given()
				.contentType(ContentType.JSON)
				.headers(headers)
				.and()
				.body(FeeCalculatorTestData.getFeeRequest())
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

	@ParameterizedTest
	@MethodSource("it.pagopa.swclient.mil.feecalculator.util.TestUtils#provideHeaderValidationErrorCases")
	@TestSecurity(user = "testUser", roles = { "NoticePayer" })
	void testGetFee_400_invalidHeaders(Map<String, String> invalidHeaders, String errorCode) {

		Response response = given()
				.contentType(ContentType.JSON)
				.headers(invalidHeaders)
				.and()
				.body(FeeCalculatorTestData.getFeeRequest())
				.when()
				.post()
				.then()
				.extract()
				.response();

		Assertions.assertEquals(400, response.statusCode());
		Assertions.assertEquals(1, response.jsonPath().getList("errors").size());
		Assertions.assertTrue(response.jsonPath().getList("errors").contains(errorCode));
		Assertions.assertNull(response.jsonPath().getJsonObject("outcome"));
		Assertions.assertNull(response.jsonPath().getJsonObject("amount"));
		Assertions.assertNull(response.jsonPath().getJsonObject("dueDate"));
		Assertions.assertNull(response.jsonPath().getJsonObject("note"));
		Assertions.assertNull(response.jsonPath().getJsonObject("description"));
		Assertions.assertNull(response.jsonPath().getJsonObject("company"));
		Assertions.assertNull(response.jsonPath().getJsonObject("office"));
	}

	@ParameterizedTest
	@MethodSource("it.pagopa.swclient.mil.feecalculator.util.TestUtils#provideGetFeeErrorCases")
	@TestSecurity(user = "testUser", roles = { "NoticePayer" })
	void testGetFee_400_invalidRequest(GetFeeRequest getFeeRequest, String errorCode) {

		Response response = given()
				.contentType(ContentType.JSON)
				.headers(milHeaders)
				.and()
				.body(getFeeRequest)
				.when()
				.post()
				.then()
				.extract()
				.response();

		Assertions.assertEquals(400, response.statusCode());
		Assertions.assertEquals(1, response.jsonPath().getList("errors").size());
		Assertions.assertTrue(response.jsonPath().getList("errors").contains(errorCode));
		Assertions.assertNull(response.jsonPath().getJsonObject("outcome"));
		Assertions.assertNull(response.jsonPath().getJsonObject("amount"));
		Assertions.assertNull(response.jsonPath().getJsonObject("dueDate"));
		Assertions.assertNull(response.jsonPath().getJsonObject("note"));
		Assertions.assertNull(response.jsonPath().getJsonObject("description"));
		Assertions.assertNull(response.jsonPath().getJsonObject("company"));
		Assertions.assertNull(response.jsonPath().getJsonObject("office"));
	}

	@Test
	@TestSecurity(user = "testUser", roles = { "Nodo" })
	void testGetFee_403_forbidden() {

		GetFeeRequest getFeeRequest = FeeCalculatorTestData.getFeeRequest();

		Mockito
				.when(milRestService.getPspConfiguration(Mockito.any(String.class)))
				.thenReturn(Uni.createFrom().item(acquirerConfiguration));

		Mockito
				.when(feeService.getFees(Mockito.any(String.class), Mockito.any(GecGetFeesRequest.class)))
				.thenReturn(Uni.createFrom().item(gecGetFeesResponse));

		Response response = given()
				.contentType(ContentType.JSON)
				.headers(milHeaders)
				.and()
				.body(getFeeRequest)
				.when()
				.post()
				.then()
				.extract()
				.response();

		Assertions.assertEquals(403, response.statusCode());

		Assertions.assertEquals(0, response.body().asString().length());


	}

	@Test
	@TestSecurity(user = "testUser", roles = { "NoticePayer" })
	void testGetFee_404_gecEmptyBundleList() {

		GecGetFeesResponse gecEmptyResponse = new GecGetFeesResponse();
		gecEmptyResponse.setBelowThreshold(false);
		gecEmptyResponse.setBundleOptions(new ArrayList<>());

		Mockito
				.when(milRestService.getPspConfiguration(Mockito.any(String.class)))
				.thenReturn(Uni.createFrom().item(acquirerConfiguration));

		Mockito
				.when(feeService.getFees(Mockito.any(String.class), Mockito.any(GecGetFeesRequest.class)))
				.thenReturn(Uni.createFrom().item(gecEmptyResponse));

		Response response = given()
				.contentType(ContentType.JSON)
				.headers(milHeaders)
				.and()
				.body(FeeCalculatorTestData.getFeeRequest())
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
	@TestSecurity(user = "testUser", roles = { "NoticePayer" })
	void testGetFee_500_gecError() {

		Mockito
				.when(milRestService.getPspConfiguration(Mockito.any(String.class)))
				.thenReturn(Uni.createFrom().item(acquirerConfiguration));

		Mockito
				.when(feeService.getFees(Mockito.any(String.class), Mockito.any(GecGetFeesRequest.class)))
				.thenReturn(Uni.createFrom().failure(new ClientWebApplicationException(404)));

		Response response = given()
				.contentType(ContentType.JSON)
				.headers(milHeaders)
				.and()
				.body(FeeCalculatorTestData.getFeeRequest())
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
	@TestSecurity(user = "testUser", roles = { "NoticePayer" })
	void testGetFee_500_pspNotFound() {

		Mockito
				.when(milRestService.getPspConfiguration(Mockito.any(String.class)))
				.thenReturn(Uni.createFrom().failure(new ClientWebApplicationException(404)));

		Response response = given()
				.contentType(ContentType.JSON)
				.headers(milHeaders)
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

	@Test
	@TestSecurity(user = "testUser", roles = { "NoticePayer" })
	void testGetFee_500_milTimeout() {

		Mockito
				.when(milRestService.getPspConfiguration(Mockito.any(String.class)))
				.thenReturn(Uni.createFrom().failure(new TimeoutException()));

		Response response = given()
				.contentType(ContentType.JSON)
				.headers(milHeaders)
				.and()
				.body(FeeCalculatorTestData.getFeeRequest())
				.when()
				.post()
				.then()
				.extract()
				.response();

		Assertions.assertEquals(500, response.statusCode());
		Assertions.assertTrue(response.jsonPath().getList("errors").contains(ErrorCode.ERROR_CALLING_MIL_REST_SERVICES));
		Assertions.assertNull(response.jsonPath().getJsonObject("fee"));
	}
}
