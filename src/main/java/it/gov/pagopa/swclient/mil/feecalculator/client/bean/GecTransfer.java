package it.gov.pagopa.swclient.mil.feecalculator.client.bean;

/**
 * Details of a transfer
 */
public class GecTransfer {

	/*
	 * 
	 */
	private String creditorInstitution;

	/*
	 * 
	 */
	private Boolean digitalStamp;
	
	/*
	 * 
	 */
	private String transferCategory;


	/**
	 * Gets creditorInstitution
	 * @return value of creditorInstitution
	 */
	public String getCreditorInstitution() {
		return creditorInstitution;
	}

	/**
	 * Sets creditorInstitution
	 * @param creditorInstitution value of creditorInstitution
	 */
	public void setCreditorInstitution(String creditorInstitution) {
		this.creditorInstitution = creditorInstitution;
	}

	/**
	 * Gets digitalStamp
	 * @return value of digitalStamp
	 */
	public Boolean isDigitalStamp() {
		return digitalStamp;
	}

	/**
	 * Sets digitalStamp
	 * @param digitalStamp value of digitalStamp
	 */
	public void setDigitalStamp(Boolean digitalStamp) {
		this.digitalStamp = digitalStamp;
	}

	/**
	 * Gets transferCategory
	 * @return value of transferCategory
	 */
	public String getTransferCategory() {
		return transferCategory;
	}

	/**
	 * Sets transferCategory
	 * @param transferCategory value of transferCategory
	 */
	public void setTransferCategory(String transferCategory) {
		this.transferCategory = transferCategory;
	}

	@Override
	public String toString() {
		return new StringBuilder("GecTransfer [creditorInstitution=")
			.append(creditorInstitution)
			.append(", digitalStamp=")
			.append(digitalStamp)
			.append(", transferCategory=")
			.append(transferCategory)
			.append("]")
			.toString();
	}
}