package it.pagopa.swclient.mil.feecalculator.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import it.pagopa.swclient.mil.feecalculator.bean.GetFeeRequest;
import it.pagopa.swclient.mil.feecalculator.bean.Notice;
import it.pagopa.swclient.mil.feecalculator.bean.PaymentMethods;
import it.pagopa.swclient.mil.feecalculator.bean.Transfer;
import it.pagopa.swclient.mil.feecalculator.client.bean.ADAccessToken;
import it.pagopa.swclient.mil.feecalculator.client.bean.AcquirerConfiguration;
import it.pagopa.swclient.mil.feecalculator.client.bean.PspConfiguration;

public final class FeeCalculatorTestData {

	public static Map<String, String> getMilHeaders(boolean isPos, boolean isKnownAcquirer) {
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("RequestId", UUID.randomUUID().toString());
		headerMap.put("Version", "1.0.0");
		headerMap.put("AcquirerId", isKnownAcquirer ? FeeCalculatorTestData.ACQUIRER_ID_KNOWN : FeeCalculatorTestData.ACQUIRER_ID_NOT_KNOWN);
		headerMap.put("Channel", isPos ? "POS" : "ATM");
		headerMap.put("TerminalId", "0aB9wXyZ");
		if (isPos)
			headerMap.put("MerchantId", "28405fHfk73x88D");
		headerMap.put("SessionId", UUID.randomUUID().toString());
		return headerMap;
	}

	public static AcquirerConfiguration getAcquirerConfiguration() {
		AcquirerConfiguration acquirerConfiguration = new AcquirerConfiguration();

		PspConfiguration pspConfiguration = new PspConfiguration();
		pspConfiguration.setPsp("AGID_01");
		pspConfiguration.setBroker("97735020584");
		pspConfiguration.setChannel("97735020584_07");
		pspConfiguration.setPassword("PLACEHOLDER");

		acquirerConfiguration.setPspConfigForVerifyAndActivate(pspConfiguration);
		acquirerConfiguration.setPspConfigForGetFeeAndClosePayment(pspConfiguration);

		return acquirerConfiguration;
	}

	public static GetFeeRequest getFeeRequest() {

		GetFeeRequest getFeeRequest = new GetFeeRequest();
		getFeeRequest.setPaymentMethod(PaymentMethods.PAYMENT_CARD.name());
		getFeeRequest.setNotices(List.of(getNotice()));

		return getFeeRequest;
	}

	public static Notice getNotice() {

		Notice notice = new Notice();
		notice.setAmount(1000L);
		notice.setPaTaxCode("15376371009");
		notice.setTransfers(List.of(getTransfer(""), getTransfer("TEST_CATEGORY")));

		return notice;
	}

	public static Transfer getTransfer(String category) {

		Transfer transfer = new Transfer();
		transfer.setCategory(category);
		transfer.setPaTaxCode("15376371009");

		return transfer;
	}

	public static ADAccessToken getAzureADAccessToken() {

		ADAccessToken token = new ADAccessToken();

		token.setType("Bearer");
		token.setExpiresOn(1800);
		token.setToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6Imk2bEdrM0ZaenhSY1ViMkMzbkVRN3N5SEpsWSJ9.eyJhdWQiOiI2ZTc0MTcyYi1iZTU2LTQ4NDMtOWZmNC1lNjZhMzliYjEyZTMiLCJpc3MiOiJodHRwczovL2xvZ2luLm1pY3Jvc29mdG9ubGluZS5jb20vNzJmOTg4YmYtODZmMS00MWFmLTkxYWItMmQ3Y2QwMTFkYjQ3L3YyLjAiLCJpYXQiOjE1MzcyMzEwNDgsIm5iZiI6MTUzNzIzMTA0OCwiZXhwIjoxNTM3MjM0OTQ4LCJhaW8iOiJBWFFBaS84SUFBQUF0QWFaTG8zQ2hNaWY2S09udHRSQjdlQnE0L0RjY1F6amNKR3hQWXkvQzNqRGFOR3hYZDZ3TklJVkdSZ2hOUm53SjFsT2NBbk5aY2p2a295ckZ4Q3R0djMzMTQwUmlvT0ZKNGJDQ0dWdW9DYWcxdU9UVDIyMjIyZ0h3TFBZUS91Zjc5UVgrMEtJaWpkcm1wNjlSY3R6bVE9PSIsImF6cCI6IjZlNzQxNzJiLWJlNTYtNDg0My05ZmY0LWU2NmEzOWJiMTJlMyIsImF6cGFjciI6IjAiLCJuYW1lIjoiQWJlIExpbmNvbG4iLCJvaWQiOiI2OTAyMjJiZS1mZjFhLTRkNTYtYWJkMS03ZTRmN2QzOGU0NzQiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhYmVsaUBtaWNyb3NvZnQuY29tIiwicmgiOiJJIiwic2NwIjoiYWNjZXNzX2FzX3VzZXIiLCJzdWIiOiJIS1pwZmFIeVdhZGVPb3VZbGl0anJJLUtmZlRtMjIyWDVyclYzeERxZktRIiwidGlkIjoiNzJmOTg4YmYtODZmMS00MWFmLTkxYWItMmQ3Y2QwMTFkYjQ3IiwidXRpIjoiZnFpQnFYTFBqMGVRYTgyUy1JWUZBQSIsInZlciI6IjIuMCJ9.pj4N-w_3Us9DrBLfpCt");

		return token;
	}

	// ACQUIRER ID
	public static final String ACQUIRER_ID_KNOWN = "4585625";
	public static final String ACQUIRER_ID_NOT_KNOWN = "4585626";

	private FeeCalculatorTestData() {
	}

}
