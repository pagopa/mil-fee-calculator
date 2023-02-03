package it.gov.pagopa.swclient.mil.feecalculator.bean;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import it.gov.pagopa.swclient.mil.feecalculator.ErrorCode;

public class Transfer {
	@NotNull(message = "[" + ErrorCode.TRANSFERS_PATAXCODE_MUST_NOT_BE_NULL + "] transfers.paTaxCode must not be null")
	@Pattern(regexp = "^\\d{11}$", message = "[" + ErrorCode.TRANSFERS_PATAXCODE_MUST_MATCH_REGEXP + "] paxCode in the tranfer list must match \"{regexp}\"")
	private String paTaxCode;
	
	@NotNull(message = "[" + ErrorCode.TRANSFERS_CATEGORY_MUST_NOT_BE_NULL + "] transfers.category must not be null")
	@Pattern(regexp = "^[ -~]{0,1024}$", message = "[" + ErrorCode.TRANSFERS_CATEGORY_MUST_MATCH_REGEXP + "] paxCode must match \"{regexp}\"")
	private String category;
	
	public String getPaTaxCode() {
		return paTaxCode;
	}
	
	public void setPaTaxCode(String paTaxCode) {
		this.paTaxCode = paTaxCode;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Transfer [paTaxCode=");
		builder.append(paTaxCode);
		builder.append(", category=");
		builder.append(category);
		builder.append("]");
		return builder.toString();
	}
}