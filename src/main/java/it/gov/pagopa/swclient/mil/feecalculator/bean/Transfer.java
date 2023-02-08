
package it.gov.pagopa.swclient.mil.feecalculator.bean;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import it.gov.pagopa.swclient.mil.feecalculator.ErrorCode;

public class Transfer {
	/*
	 * Tax code of the creditor company
	 */
	@NotNull(message = "[" + ErrorCode.TRANSFERS_PATAXCODE_MUST_NOT_BE_NULL + "] transfers.paTaxCode must not be null")
	@Pattern(regexp = "^\\d{11}$", message = "[" + ErrorCode.TRANSFERS_PATAXCODE_MUST_MATCH_REGEXP + "] paxCode in the tranfer list must match \"{regexp}\"")
	private String paTaxCode;
	
	/*
	 * Transfer category
	 */
	@NotNull(message = "[" + ErrorCode.TRANSFERS_CATEGORY_MUST_NOT_BE_NULL + "] transfers.category must not be null")
	@Pattern(regexp = "^[ -~]{0,1024}$", message = "[" + ErrorCode.TRANSFERS_CATEGORY_MUST_MATCH_REGEXP + "] paxCode must match \"{regexp}\"")
	private String category;

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
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
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