package it.gov.pagopa.swclient.mil.feecalculator.bean;

import java.util.List;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import it.gov.pagopa.swclient.mil.feecalculator.ErrorCode;

public class FeeBody {
	@Pattern(regexp = "PAGOBANCOMAT|DEBIT_CARD|CREDIT_CARD|BANK_ACCOUNT|CASH", message = "[" + ErrorCode.ERROR_PAYMENT_METHOD + "] paymentMethod must match one of the values \"{regexp}\"")
	private String paymentMethod;
	
	@Size(max = 5, message = "[" + ErrorCode.ERROR_NOTICES_LIST_EXCEEDED + "] the max size of notices is 5")
	private List<Notice> notices;

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public List<Notice> getNotices() {
		return notices;
	}

	public void setNotices(List<Notice> notices) {
		this.notices = notices;
	}

	@Override
	public String toString() {
		return "FeeBody [paymentMethod=" + paymentMethod + ", notices=" + notices + "]";
	}
}
