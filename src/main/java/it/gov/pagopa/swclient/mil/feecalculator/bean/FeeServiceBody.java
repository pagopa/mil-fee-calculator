package it.gov.pagopa.swclient.mil.feecalculator.bean;

import java.util.List;

public class FeeServiceBody {
	private long paymentAmount;
	private String primaryCreditorIntitution;
	private String paymentMethod;
	private String touchpoint;
	private List<TransferForFeeService> transferList;
	public long getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(long paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public String getPrimaryCreditorIntitution() {
		return primaryCreditorIntitution;
	}
	public void setPrimaryCreditorIntitution(String primaryCreditorIntitution) {
		this.primaryCreditorIntitution = primaryCreditorIntitution;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getTouchpoint() {
		return touchpoint;
	}
	public void setTouchpoint(String touchpoint) {
		this.touchpoint = touchpoint;
	}
	public List<TransferForFeeService> getTransferList() {
		return transferList;
	}
	public void setTransferList(List<TransferForFeeService> transferList) {
		this.transferList = transferList;
	}
	@Override
	public String toString() {
		return "FeeServiceBody [paymentAmount=" + paymentAmount + ", primaryCreditorIntitution="
				+ primaryCreditorIntitution + ", paymentMethod=" + paymentMethod + ", touchpoint=" + touchpoint + "]";
	}
}
