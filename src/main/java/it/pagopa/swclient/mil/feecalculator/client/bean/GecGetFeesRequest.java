/**
 * request to the GEC service
 */
package it.pagopa.swclient.mil.feecalculator.client.bean;

import java.util.List;

import jakarta.validation.constraints.NotNull;

/**
 * The request to the getFees API exposed by GEC
 */
public class GecGetFeesRequest {

	/**
	 * 
	 */
	private List<Psp> idPspList;

	/**
	 * 
	 */
	@NotNull
	private Long paymentAmount;

	/**
	 * 
	 */
	@NotNull
	private String primaryCreditorInstitution;

	/**
	 * 
	 */
	private String paymentMethod;

	/**
	 * 
	 */
	private String touchpoint;

	/**
	 * 
	 */
	@NotNull
	private List<GecTransfer> transferList;

	/**
	 * @return the idPspList
	 */
	public List<Psp> getIdPspList() {
		return idPspList;
	}

	/**
	 * @param idPspList the idPspList to set
	 */
	public void setIdPspList(List<Psp> idPspList) {
		this.idPspList = idPspList;
	}

	/**
	 * Gets paymentAmount
	 * 
	 * @return value of paymentAmount
	 */
	public Long getPaymentAmount() {
		return paymentAmount;
	}

	/**
	 * Sets paymentAmount
	 * 
	 * @param paymentAmount value of paymentAmount
	 */
	public void setPaymentAmount(Long paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	/**
	 * Gets primaryCreditorInstitution
	 * 
	 * @return value of primaryCreditorInstitution
	 */
	public String getPrimaryCreditorInstitution() {
		return primaryCreditorInstitution;
	}

	/**
	 * Sets primaryCreditorInstitution
	 * 
	 * @param primaryCreditorInstitution value of primaryCreditorInstitution
	 */
	public void setPrimaryCreditorInstitution(String primaryCreditorInstitution) {
		this.primaryCreditorInstitution = primaryCreditorInstitution;
	}

	/**
	 * Gets paymentMethod
	 * 
	 * @return value of paymentMethod
	 */
	public String getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * Sets paymentMethod
	 * 
	 * @param paymentMethod value of paymentMethod
	 */
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	/**
	 * Gets touchpoint
	 * 
	 * @return value of touchpoint
	 */
	public String getTouchpoint() {
		return touchpoint;
	}

	/**
	 * Sets touchpoint
	 * 
	 * @param touchpoint value of touchpoint
	 */
	public void setTouchpoint(String touchpoint) {
		this.touchpoint = touchpoint;
	}

	/**
	 * Gets transferList
	 * 
	 * @return value of transferList
	 */
	public List<GecTransfer> getTransferList() {
		return transferList;
	}

	/**
	 * Sets transferList
	 * 
	 * @param transferList value of transferList
	 */
	public void setTransferList(List<GecTransfer> transferList) {
		this.transferList = transferList;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GecGetFeesRequest [idPspList=");
		builder.append(idPspList);
		builder.append(", paymentAmount=");
		builder.append(paymentAmount);
		builder.append(", primaryCreditorInstitution=");
		builder.append(primaryCreditorInstitution);
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