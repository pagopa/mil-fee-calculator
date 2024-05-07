package it.pagopa.swclient.mil.feecalculator.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;

import org.jboss.resteasy.reactive.ClientWebApplicationException;
import org.junit.jupiter.params.provider.Arguments;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;

import it.pagopa.swclient.mil.feecalculator.ErrorCode;
import it.pagopa.swclient.mil.feecalculator.bean.Notice;
import it.pagopa.swclient.mil.feecalculator.bean.Transfer;
import jakarta.ws.rs.core.Response;

public class TestUtils {

	private TestUtils() {
	}

	public static Stream<Arguments> provideMilIntegrationErrorCases() {
		return Stream.of(
			Arguments.of(ExceptionType.CLIENT_WEB_APPLICATION_EXCEPTION_400, ErrorCode.ERROR_CALLING_MIL_REST_SERVICES),
			Arguments.of(ExceptionType.CLIENT_WEB_APPLICATION_EXCEPTION_404, ErrorCode.UNKNOWN_ACQUIRER_ID),
			Arguments.of(ExceptionType.CLIENT_WEB_APPLICATION_EXCEPTION_500, ErrorCode.ERROR_CALLING_MIL_REST_SERVICES),
			Arguments.of(ExceptionType.TIMEOUT_EXCEPTION, ErrorCode.ERROR_CALLING_MIL_REST_SERVICES),
			Arguments.of(ExceptionType.UNPARSABLE_EXCEPTION, ErrorCode.ERROR_CALLING_MIL_REST_SERVICES));
	}

	public static Stream<Arguments> provideHeaderValidationErrorCases() {
		return Stream.of(
			// RequestId null
			Arguments.of(removeAndGet(FeeCalculatorTestData.getMilHeaders(false, true), "RequestId"), it.pagopa.swclient.mil.ErrorCode.REQUEST_ID_MUST_NOT_BE_NULL),
			// RequestId invalid regex
			Arguments.of(putAndGet(FeeCalculatorTestData.getMilHeaders(false, true), "RequestId", "dmmmm0d654e6-97da-4848-b568-99fedccb642ba"), it.pagopa.swclient.mil.ErrorCode.REQUEST_ID_MUST_MATCH_REGEXP),
			// Version longer than max size
			Arguments.of(putAndGet(FeeCalculatorTestData.getMilHeaders(false, true), "Version", "1.0.0-alpha-a.b-c-somethinglong+build.1-aef.1-its-okayokayokayokayokayokayokayokay"), it.pagopa.swclient.mil.ErrorCode.VERSION_SIZE_MUST_BE_AT_MOST_MAX),
			// Version invalid regex
			Arguments.of(putAndGet(FeeCalculatorTestData.getMilHeaders(false, true), "Version", ".1.0.0-alpha-a.b-c-somethinglong+build.1-aef.1-its-okay"), it.pagopa.swclient.mil.ErrorCode.VERSION_MUST_MATCH_REGEXP),
			// AcquirerId null
			Arguments.of(removeAndGet(FeeCalculatorTestData.getMilHeaders(false, true), "AcquirerId"), it.pagopa.swclient.mil.ErrorCode.ACQUIRER_ID_MUST_NOT_BE_NULL),
			// AcquirerId invalid regex
			Arguments.of(putAndGet(FeeCalculatorTestData.getMilHeaders(false, true), "AcquirerId", "45856bb25"), it.pagopa.swclient.mil.ErrorCode.ACQUIRER_ID_MUST_MATCH_REGEXP),
			// Channel null
			Arguments.of(removeAndGet(FeeCalculatorTestData.getMilHeaders(false, true), "Channel"), it.pagopa.swclient.mil.ErrorCode.CHANNEL_MUST_NOT_BE_NULL),
			// Channel invalid regex
			Arguments.of(putAndGet(FeeCalculatorTestData.getMilHeaders(false, true), "Channel", "ATOM"), it.pagopa.swclient.mil.ErrorCode.CHANNEL_MUST_MATCH_REGEXP),
			// TerminalId null
			Arguments.of(removeAndGet(FeeCalculatorTestData.getMilHeaders(false, true), "TerminalId"), it.pagopa.swclient.mil.ErrorCode.TERMINAL_ID_MUST_NOT_BE_NULL),
			// TerminalId invalid regex
			Arguments.of(putAndGet(FeeCalculatorTestData.getMilHeaders(false, true), "TerminalId", "0aB9wXyZ0029DDDsno9"), it.pagopa.swclient.mil.ErrorCode.TERMINAL_ID_MUST_MATCH_REGEXP),
			// Merchant invalid regex
			Arguments.of(putAndGet(FeeCalculatorTestData.getMilHeaders(true, true), "MerchantId", "0aB9wXyZ00_29DDDsno9"), it.pagopa.swclient.mil.ErrorCode.MERCHANT_ID_MUST_MATCH_REGEXP),
			// Merchant null if pos
			Arguments.of(removeAndGet(FeeCalculatorTestData.getMilHeaders(true, true), "MerchantId"), it.pagopa.swclient.mil.ErrorCode.MERCHANT_ID_MUST_NOT_BE_NULL_FOR_POS));
	}

	public static Stream<Arguments> provideGetFeeErrorCases() {

		List<Notice> notices = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			notices.add(FeeCalculatorTestData.getNotice());
		}

		List<Transfer> transfers = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			transfers.add(FeeCalculatorTestData.getTransfer("TEST_GATEGORY"));
		}

		return Stream.of(
			Arguments.of(setAndGet(FeeCalculatorTestData.getFeeRequest(), "paymentMethod", "TEST"), ErrorCode.PAYMENT_METHOD_MUST_MATCH_REGEXP),
			Arguments.of(setAndGet(FeeCalculatorTestData.getFeeRequest(), "notices", null), ErrorCode.NOTICES_MUST_NOT_BE_NULL),
			Arguments.of(setAndGet(FeeCalculatorTestData.getFeeRequest(), "notices", notices), ErrorCode.NOTICES_LIST_EXCEEDED_SIZE),
			// notice validation
			Arguments.of(setAndGet(FeeCalculatorTestData.getFeeRequest(), "notices",
				List.of(setAndGet(FeeCalculatorTestData.getNotice(), "amount", null))), ErrorCode.AMOUNT_MUST_NOT_BE_NULL),
			Arguments.of(setAndGet(FeeCalculatorTestData.getFeeRequest(), "notices",
				List.of(setAndGet(FeeCalculatorTestData.getNotice(), "amount", 0L))), ErrorCode.AMOUNT_EXCEEDED_MIN_VALUE),
			Arguments.of(setAndGet(FeeCalculatorTestData.getFeeRequest(), "notices",
				List.of(setAndGet(FeeCalculatorTestData.getNotice(), "amount", 999999999999L))), ErrorCode.AMOUNT_EXCEED_MAX_VALUE),
			Arguments.of(setAndGet(FeeCalculatorTestData.getFeeRequest(), "notices",
				List.of(setAndGet(FeeCalculatorTestData.getNotice(), "paTaxCode", null))), ErrorCode.PA_TAX_CODE_MUST_NOT_BE_NULL),
			Arguments.of(setAndGet(FeeCalculatorTestData.getFeeRequest(), "notices",
				List.of(setAndGet(FeeCalculatorTestData.getNotice(), "paTaxCode", "ABC"))), ErrorCode.PA_TAX_CODE_MUST_MATCH_REGEXP),
			Arguments.of(setAndGet(FeeCalculatorTestData.getFeeRequest(), "notices",
				List.of(setAndGet(FeeCalculatorTestData.getNotice(), "transfers", null))), ErrorCode.TRANSFERS_MUST_NOT_BE_NULL),
			Arguments.of(setAndGet(FeeCalculatorTestData.getFeeRequest(), "notices",
				List.of(setAndGet(FeeCalculatorTestData.getNotice(), "transfers", transfers))), ErrorCode.TRANSFERS_EXCEEDED_MAX_VALUE),
			// transfer validation
			Arguments.of(setAndGet(FeeCalculatorTestData.getFeeRequest(), "notices",
				List.of(setAndGet(FeeCalculatorTestData.getNotice(), "transfers",
					List.of(setAndGet(FeeCalculatorTestData.getTransfer(""), "paTaxCode", null))))),
				ErrorCode.TRANSFERS_PA_TAX_CODE_MUST_NOT_BE_NULL),
			Arguments.of(setAndGet(FeeCalculatorTestData.getFeeRequest(), "notices",
				List.of(setAndGet(FeeCalculatorTestData.getNotice(), "transfers",
					List.of(setAndGet(FeeCalculatorTestData.getTransfer(""), "paTaxCode", "ABC"))))),
				ErrorCode.TRANSFERS_PA_TAX_CODE_MUST_MATCH_REGEXP),
			Arguments.of(setAndGet(FeeCalculatorTestData.getFeeRequest(), "notices",
				List.of(setAndGet(FeeCalculatorTestData.getNotice(), "transfers",
					List.of(setAndGet(FeeCalculatorTestData.getTransfer(""), "category", null))))),
				ErrorCode.TRANSFERS_CATEGORY_MUST_NOT_BE_NULL),
			Arguments.of(setAndGet(FeeCalculatorTestData.getFeeRequest(), "notices",
				List.of(setAndGet(FeeCalculatorTestData.getNotice(), "transfers",
					List.of(setAndGet(FeeCalculatorTestData.getTransfer(""), "category", "€"))))),
				ErrorCode.TRANSFERS_CATEGORY_MUST_MATCH_REGEXP));
	}
	//
	// public static Stream<Arguments> providePreCloseRequestValidationErrorCases() {
	//
	// return Stream.of(
	// Arguments.of(setAndGet(PaymentTestData.getPreCloseRequest(true, 1, false), "outcome", null),
	// ErrorCode.ERROR_OUTCOME_MUST_NOT_BE_NULL),
	// Arguments.of(setAndGet(PaymentTestData.getPreCloseRequest(true, 1, false), "outcome", "OK"),
	// ErrorCode.ERROR_OUTCOME_MUST_MATCH_MATCH_REGEXP),
	// Arguments.of(setAndGet(PaymentTestData.getPreCloseRequest(true, 1, false), "paymentTokens",
	// null), ErrorCode.ERROR_PAYMENT_TOKEN_LIST_MUST_NOT_BE_NULL),
	// Arguments.of(setAndGet(PaymentTestData.getPreCloseRequest(true, 1, false), "paymentTokens",
	// List.of("100","101","102","103","104","105")),
	// ErrorCode.ERROR_PAYMENT_TOKEN_LIST_MUST_HAVE_AT_MOST),
	// Arguments.of(setAndGet(PaymentTestData.getPreCloseRequest(true, 1, false), "paymentTokens",
	// List.of("123456789012345678901234567890123456")),
	// ErrorCode.ERROR_PAYMENT_TOKEN_MATCH_MATCH_REGEXP),
	// Arguments.of(setAndGet(PaymentTestData.getPreCloseRequest(true, 1, false), "transactionId",
	// null), ErrorCode.ERROR_TRANSACTION_ID_MUST_NOT_BE_NULL),
	// Arguments.of(setAndGet(PaymentTestData.getPreCloseRequest(true, 1, false), "transactionId",
	// "abc"), ErrorCode.ERROR_TRANSACTION_ID_MUST_MATCH_REGEXP),
	// Arguments.of(setAndGet(PaymentTestData.getPreCloseRequest(true, 1, false), "totalAmount", null),
	// ErrorCode.ERROR_TOTAL_AMOUNT_MUST_NOT_BE_NULL),
	// Arguments.of(setAndGet(PaymentTestData.getPreCloseRequest(true, 1, false), "totalAmount", 0L),
	// ErrorCode.ERROR_TOTAL_AMOUNT_MUST_BE_GREATER_THAN),
	// Arguments.of(setAndGet(PaymentTestData.getPreCloseRequest(true, 1, false), "totalAmount",
	// 199999999999L), ErrorCode.ERROR_TOTAL_AMOUNT_MUST_BE_LESS_THAN),
	// Arguments.of(setAndGet(PaymentTestData.getPreCloseRequest(true, 1, false), "totalAmount",
	// 12345L), ErrorCode.ERROR_TOTAL_AMOUNT_MUST_MATCH_TOTAL_CACHED_VALUE),
	// Arguments.of(setAndGet(PaymentTestData.getPreCloseRequest(true, 1, false), "fee", null),
	// ErrorCode.ERROR_FEE_MUST_NOT_BE_NULL),
	// Arguments.of(setAndGet(PaymentTestData.getPreCloseRequest(true, 1, false), "fee", 0L),
	// ErrorCode.ERROR_FEE_MUST_BE_GREATER_THAN),
	// Arguments.of(setAndGet(PaymentTestData.getPreCloseRequest(true, 1, false), "fee", 199999999999L),
	// ErrorCode.ERROR_FEE_MUST_BE_LESS_THAN),
	// // preset test data
	// Arguments.of(setAndGet(PaymentTestData.getPreCloseRequest(true, 1, false),
	// "preset", setAndGet(PaymentTestData.getPreset(), "paTaxCode", null)),
	// ErrorCode.PA_TAX_CODE_MUST_NOT_BE_NULL),
	// Arguments.of(setAndGet(PaymentTestData.getPreCloseRequest(true, 1, false),
	// "preset", setAndGet(PaymentTestData.getPreset(), "paTaxCode", "abcde")),
	// ErrorCode.PA_TAX_CODE_MUST_MATCH_REGEXP),
	// Arguments.of(setAndGet(PaymentTestData.getPreCloseRequest(true, 1, false),
	// "preset", setAndGet(PaymentTestData.getPreset(), "subscriberId", null)),
	// ErrorCode.SUBSCRIBER_ID_CODE_MUST_NOT_BE_NULL),
	// Arguments.of(setAndGet(PaymentTestData.getPreCloseRequest(true, 1, false),
	// "preset", setAndGet(PaymentTestData.getPreset(), "subscriberId", "abcde")),
	// ErrorCode.SUBSCRIBER_ID_MUST_MATCH_REGEXP),
	// Arguments.of(setAndGet(PaymentTestData.getPreCloseRequest(true, 1, false),
	// "preset", setAndGet(PaymentTestData.getPreset(), "presetId", null)),
	// ErrorCode.PRESET_ID_MUST_NOT_BE_NULL),
	// Arguments.of(setAndGet(PaymentTestData.getPreCloseRequest(true, 1, false),
	// "preset", setAndGet(PaymentTestData.getPreset(), "presetId", "abcde")),
	// ErrorCode.PRESET_ID_MUST_MATCH_REGEXP)
	// );
	// }

	public static Throwable getException(ExceptionType exceptionType) {
		return switch (exceptionType) {
		case TIMEOUT_EXCEPTION -> new TimeoutException();
		case CLIENT_WEB_APPLICATION_EXCEPTION_400 -> new ClientWebApplicationException(400);
		case CLIENT_WEB_APPLICATION_EXCEPTION_404 -> new ClientWebApplicationException(404);
		case CLIENT_WEB_APPLICATION_EXCEPTION_500 -> new ClientWebApplicationException(500);
		case UNPARSABLE_EXCEPTION -> new ClientWebApplicationException(new JsonParseException(getJsonParser(), ""));
		};
	}

	public static ClientWebApplicationException getExceptionWithEntity(int statusCode) {
		return new ClientWebApplicationException(Response.status(statusCode).entity("").build());
	}

	private static JsonParser getJsonParser() {
		JsonParser jsonParser = null;
		try {
			jsonParser = new JsonFactory().createParser("{}");
		} catch (IOException ignored) {
		}
		return jsonParser;
	}

	private static <K, V> Map<K, V> removeAndGet(Map<K, V> map, K key) {
		map.remove(key);
		return map;
	}

	private static <K, V> Map<K, V> putAndGet(Map<K, V> map, K key, V value) {
		map.put(key, value);
		return map;
	}

	private static <T, V> T setAndGet(T object, String propertyName, V propertyValue) {

		try {
			PropertyDescriptor desc = new PropertyDescriptor(propertyName, object.getClass());
			Method setter = desc.getWriteMethod();
			setter.invoke(object, propertyValue);
		} catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}

		return object;
	}

}
