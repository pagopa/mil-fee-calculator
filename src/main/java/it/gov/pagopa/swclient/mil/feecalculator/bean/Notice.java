
package it.gov.pagopa.swclient.mil.feecalculator.bean;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import it.gov.pagopa.swclient.mil.feecalculator.ErrorCode;

public class Notice {
	
	/*
	 * Amount in euro cents
	 */
	@NotNull(message = "[" + ErrorCode.AMOUNT_MUST_NOT_BE_NULL + "] amount must not be null")
	@Min(value = 1, message = "[" + ErrorCode.AMOUNT_EXCEEDED_MIN_VALUE + "] amount must be between 1 and 99999999999" )
	@Max(value = 99999999999L, message = "[" + ErrorCode.AMOUNT_EXEED_MAX_VALUE + "] must be between 1 and 99999999999" )
	private Long amount;
	
	/*
	 * Tax code of the creditor company
	 */
	@NotNull(message = "[" + ErrorCode.PATAXCODE_MUST_NOT_BE_NULL + "] paTaxCode must not be null")
	@Pattern(regexp = "^\\d{11}$", message = "[" + ErrorCode.PATAXCODE_MUST_MATCH_REGEXP + "] paxCode must match \"{regexp}\"")
	private String paTaxCode;
	
	/*
	 * 	Transfer essential data
	 */
	@Valid
	@NotNull(message = "[" + ErrorCode.TRANSFERS_MUST_NOT_BE_NULL + "] transfers must not be null")
	@Size(max = 5, message = "[" + ErrorCode.TRANSFERS_EXCEEDED_MAX_VALUE + "] size must be between 1 and 5")
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
