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
	
	@NotNull(message = "[" + ErrorCode.AMOUNT_MUST_NOT_BE_NULL + "] amount must not be null")
	@Min(value = 1, message = "[" + ErrorCode.AMOUNT_EXCEEDED_MIN_VALUE + "] amount must be between 1 and 99999999999" )
	@Max(value = 99999999999L, message = "[" + ErrorCode.AMOUNT_EXEED_MAX_VALUE + "] must be between 1 and 99999999999" )
	private Long amount;
	
	@NotNull(message = "[" + ErrorCode.PATAXCODE_MUST_NOT_BE_NULL + "] paTaxCode must not be null")
	@Pattern(regexp = "^\\d{11}$", message = "[" + ErrorCode.PATAXCODE_MUST_MATCH_REGEXP + "] paxCode must match \"{regexp}\"")
	private String paTaxCode;
	
	@Valid
	@NotNull(message = "[" + ErrorCode.TRANSFERS_MUST_NOT_BE_NULL + "] transfers must not be null")
	@Size(max = 5, message = "[" + ErrorCode.TRANSFERS_EXCEEDED_MAX_VALUE + "] size must be between 1 and 5")
	private List<Transfer> transfers;
	
	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getPaTaxCode() {
		return paTaxCode;
	}

	public void setPaTaxCode(String paTaxCode) {
		this.paTaxCode = paTaxCode;
	}

	public List<Transfer> getTransfers() {
		return transfers;
	}

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
