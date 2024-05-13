
package it.pagopa.swclient.mil.feecalculator.bean;

import java.util.List;

import it.pagopa.swclient.mil.feecalculator.ErrorCode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Details of a payment notice
 */
public class Notice {

	/**
	 * Amount in euro cents
	 */
	@NotNull(message = "[" + ErrorCode.AMOUNT_MUST_NOT_BE_NULL + "] amount must not be null")
	@Min(value = 1, message = "[" + ErrorCode.AMOUNT_EXCEEDED_MIN_VALUE + "] amount must not be less than {value}")
	@Max(value = 99999999999L, message = "[" + ErrorCode.AMOUNT_EXCEED_MAX_VALUE + "] must must not be greater than {value}")
	private Long amount;

	/**
	 * Tax code of the creditor company
	 */
	@NotNull(message = "[" + ErrorCode.PA_TAX_CODE_MUST_NOT_BE_NULL + "] paTaxCode must not be null")
	@Pattern(regexp = "^\\d{11}$", message = "[" + ErrorCode.PA_TAX_CODE_MUST_MATCH_REGEXP + "] paxCode must match \"{regexp}\"")
	private String paTaxCode;

	/**
	 * Transfer essential data
	 */
	@Valid
	@NotNull(message = "[" + ErrorCode.TRANSFERS_MUST_NOT_BE_NULL + "] transfers must not be null")
	@Size(max = 5, message = "[" + ErrorCode.TRANSFERS_EXCEEDED_MAX_VALUE + "] transfers must contain at most {max} elements")
	private List<Transfer> transfers;

	/**
	 * @return the amount
	 */
	public Long getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Long amount) {
		this.amount = amount;
	}

	/**
	 * @return the paTaxCode
	 */
	public String getPaTaxCode() {
		return paTaxCode;
	}

	/**
	 * @param paTaxCode the paTaxCode to set
	 */
	public void setPaTaxCode(String paTaxCode) {
		this.paTaxCode = paTaxCode;
	}

	/**
	 * @return the transfers
	 */
	public List<Transfer> getTransfers() {
		return transfers;
	}

	/**
	 * @param transfers the transfers to set
	 */
	public void setTransfers(List<Transfer> transfers) {
		this.transfers = transfers;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Notice [amount=");
		builder.append(amount);
		builder.append(", paTaxCode=");
		builder.append(paTaxCode);
		builder.append(", transfers=");
		builder.append(transfers);
		builder.append("]");
		return builder.toString();
	}
}
