package it.gov.pagopa.swclient.mil.feecalculator.bean;

import java.util.List;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import it.gov.pagopa.swclient.mil.feecalculator.ErrorCode;

public class Notice {
	private long amount;
	
	@Pattern(regexp = "^\\d{11}$", message = "[" + ErrorCode.ERROR_PAX_CODE_INVALID + "] paxCode must match \"{regexp}\"")
	private String paTaxCode;
	
	@Size(max = 5, message = "[" + ErrorCode.ERROR_TRANSFERS_LIST_EXCEEDED + "] the max size of tranfers is 5")
	private List<Transfer> transfers;
	
	@AssertTrue(message = "[" + ErrorCode.ERROR_INVALID_AMOUNT + "] amount passed in the body is not valid")
    private boolean isTotalAmountValid() {
        return amount >= 1 && amount <= 99999999999L;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
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
		return "Notice [amount=" + amount + ", paTaxCode=" + paTaxCode + ", transfers=" + transfers + "]";
	}
}
