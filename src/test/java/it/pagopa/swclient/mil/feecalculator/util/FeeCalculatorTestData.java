package it.pagopa.swclient.mil.feecalculator.util;

import it.pagopa.swclient.mil.feecalculator.bean.GetFeeRequest;
import it.pagopa.swclient.mil.feecalculator.bean.Notice;
import it.pagopa.swclient.mil.feecalculator.bean.PaymentMethods;
import it.pagopa.swclient.mil.feecalculator.bean.Transfer;
import it.pagopa.swclient.mil.feecalculator.client.bean.AcquirerConfiguration;
import it.pagopa.swclient.mil.feecalculator.client.bean.PspConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class FeeCalculatorTestData {

    public static Map<String, String> getMilHeaders(boolean isPos, boolean isKnownAcquirer) {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("RequestId", UUID.randomUUID().toString());
        headerMap.put("Version", "1.0.0");
        headerMap.put("AcquirerId", isKnownAcquirer ? FeeCalculatorTestData.ACQUIRER_ID_KNOWN : FeeCalculatorTestData.ACQUIRER_ID_NOT_KNOWN);
        headerMap.put("Channel", isPos ? "POS" : "ATM");
        headerMap.put("TerminalId", "0aB9wXyZ");
        if (isPos) headerMap.put("MerchantId", "28405fHfk73x88D");
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


    // ACQUIRER ID
    public static final String ACQUIRER_ID_KNOWN = "4585625";
    public static final String ACQUIRER_ID_NOT_KNOWN = "4585626";



    private FeeCalculatorTestData() {
    }


}
