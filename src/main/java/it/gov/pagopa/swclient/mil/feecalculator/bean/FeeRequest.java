package it.gov.pagopa.swclient.mil.feecalculator.bean;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import it.gov.pagopa.swclient.mil.feecalculator.ErrorCode;

public class FeeRequest {
	@NotNull(message = "[" + ErrorCode.PAYMENT_METOD_MUST_NOT_BE_NULL + "] paymentMethod must not be null")
	@Pattern(regexp = "PAGOBANCOMAT|DEBIT_CARD|CREDIT_CARD|BANK_ACCOUNT|CASH", message = "[" + ErrorCode.PAYMENT_METHOD_MUST_MATCH_REGEXP + "] paymentMethod must match one of the values \"{regexp}\"")
	private String paymentMethod;
	
	@Valid
	@NotNull(message = "[" + ErrorCode.NOTICES_MUST_NOT_BE_NULL + "] notices must not be null")
	@Size(max = 5, message = "[" + ErrorCode.NOTICES_LIST_EXCEEDED_SIZE + "] size must be between 1 and 5")
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
		StringBuilder builder = new StringBuilder();
		builder.append("FeeBody [paymentMethod=");
		builder.append(paymentMethod);
		builder.append(", notices=");
		builder.append(notices);
		builder.append("]");
		return builder.toString();
	}


}
