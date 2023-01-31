package it.gov.pagopa.swclient.mil.feecalculator.bean;

import javax.validation.constraints.Pattern;

import it.gov.pagopa.swclient.mil.feecalculator.ErrorCode;

public class Transfer {
	@Pattern(regexp = "^\\d{11}$", message = "[" + ErrorCode.ERROR_PAX_CODE_INVALID + "] paxCode in the tranfer list must match \"{regexp}\"")
	private String paTaxCode;
	@Pattern(regexp = "^[ -~]{0,1024}$", message = "[" + ErrorCode.ERROR_PAX_CODE_INVALID + "] paxCode must match \"{regexp}\"")
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
		return "Transfer [paTaxCode=" + paTaxCode + ", category=" + category + "]";
	}
	
}
