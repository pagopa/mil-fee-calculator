package it.gov.pagopa.swclient.mil.feecalculator.bean.gec;

import java.util.List;

import javax.validation.constraints.NotNull;

import it.gov.pagopa.swclient.mil.feecalculator.bean.TransferForFeeService;

public class FeesGecRequest {
	private List<String> idPspList;
	
	@NotNull
	private Long paymentAmount;
	
	@NotNull
	private String primaryCreditorIntitution;
	
	private String paymentMethod;
	
	private String touchpoint;
	
	@NotNull
	private List<TransferForFeeService> transferList;
	
	public List<String> getIdPspList() {
		return idPspList;
	}
	
	public void setIdPspList(List<String> idPspList) {
		this.idPspList = idPspList;
	}
	
	public Long getPaymentAmount() {
		return paymentAmount;
	}
	
	public void setPaymentAmount(Long paymentAmount) {
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
		StringBuilder builder = new StringBuilder();
		builder.append("FeesGecRequest [idPspList=");
		builder.append(idPspList);
		builder.append(", paymentAmount=");
		builder.append(paymentAmount);
		builder.append(", primaryCreditorIntitution=");
		builder.append(primaryCreditorIntitution);
		builder.append(", paymentMethod=");
		builder.append(paymentMethod);
		builder.append(", touchpoint=");
		builder.append(touchpoint);
		builder.append(", transferList=");
		builder.append(transferList);
		builder.append("]");
		return builder.toString();
	}
}
