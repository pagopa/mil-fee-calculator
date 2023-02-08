package it.gov.pagopa.swclient.mil.feecalculator.bean;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import it.gov.pagopa.swclient.mil.feecalculator.ErrorCode;

/**
 * Request of the getFee API
 */
public class GetFeeRequest {

	/**
	 * Method used to pay notice/s
	 */
	@NotNull(message = "[" + ErrorCode.PAYMENT_METHOD_MUST_NOT_BE_NULL + "] paymentMethod must not be null")
	@Pattern(regexp = "PAGOBANCOMAT|DEBIT_CARD|CREDIT_CARD|BANK_ACCOUNT|CASH", message = "[" + ErrorCode.PAYMENT_METHOD_MUST_MATCH_REGEXP + "] paymentMethod must match one of the values \"{regexp}\"")
	private String paymentMethod;
	
	/**
	 * Payment notice data
	 */
	@Valid
	@NotNull(message = "[" + ErrorCode.NOTICES_MUST_NOT_BE_NULL + "] notices must not be null")
	@Size(max = 5, message = "[" + ErrorCode.NOTICES_LIST_EXCEEDED_SIZE + "] notices must contain at most {max} elements")
	private List<Notice> notices;

	/**
	 * @return the paymentMethod
	 */
	public String getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * @param paymentMethod the paymentMethod to set
	 */
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	/**
	 * @return the notices
	 */
	public List<Notice> getNotices() {
		return notices;
	}

	/**
	 * @param notices the notices to set
	 */
	public void setNotices(List<Notice> notices) {
		this.notices = notices;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("GetFeeRequest{");
		sb.append("paymentMethod='").append(paymentMethod).append('\'');
		sb.append(", notices=").append(notices);
		sb.append('}');
		return sb.toString();
	}

}
