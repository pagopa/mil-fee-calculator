/**
 * request to the GEC service
 */
package it.gov.pagopa.swclient.mil.feecalculator.bean.gec;

import java.util.List;

import javax.validation.constraints.NotNull;

public class FeesGecRequest {
	
	/*
	 * 
	 */
	private List<String> idPspList;
	
	/*
	 * 
	 */
	@NotNull
	private Long paymentAmount;
	
	/*
	 * 
	 */
	@NotNull
	private String primaryCreditorIntitution;
	
	/*
	 * 
	 */
	private String paymentMethod;
	
	/*
	 * 
	 */
	private String touchpoint;
	
	/*
	 * 
	 */
	@NotNull
	private List<TransferForFeeService> transferList;
	
	/**
	 * @return the idPspList
	 */
	public List<String> getIdPspList() {
		return idPspList;
	}

	/**
	 * @param idPspList the idPspList to set
	 */
	public void setIdPspList(List<String> idPspList) {
		this.idPspList = idPspList;
	}

	/**
	 * @return the paymentAmount
	 */
	public Long getPaymentAmount() {
		return paymentAmount;
	}

	/**
	 * @param paymentAmount the paymentAmount to set
	 */
	public void setPaymentAmount(Long paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	/**
	 * @return the primaryCreditorIntitution
	 */
	public String getPrimaryCreditorIntitution() {
		return primaryCreditorIntitution;
	}

	/**
	 * @param primaryCreditorIntitution the primaryCreditorIntitution to set
	 */
	public void setPrimaryCreditorIntitution(String primaryCreditorIntitution) {
		this.primaryCreditorIntitution = primaryCreditorIntitution;
	}

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
	 * @return the touchpoint
	 */
	public String getTouchpoint() {
		return touchpoint;
	}

	/**
	 * @param touchpoint the touchpoint to set
	 */
	public void setTouchpoint(String touchpoint) {
		this.touchpoint = touchpoint;
	}

	/**
	 * @return the transferList
	 */
	public List<TransferForFeeService> getTransferList() {
		return transferList;
	}

	/**
	 * @param transferList the transferList to set
	 */
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
