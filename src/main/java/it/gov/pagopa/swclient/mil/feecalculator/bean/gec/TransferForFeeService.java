package it.gov.pagopa.swclient.mil.feecalculator.bean.gec;

public class TransferForFeeService {
	/*
	 * 
	 */
	private String creditorInstitution;

	/*
	 * 
	 */
	private boolean digitalStamp;
	
	/*
	 * 
	 */
	private String tranferCategory;
	
	/**
	 * @return the creditorInstitution
	 */
	public String getCreditorInstitution() {
		return creditorInstitution;
	}

	/**
	 * @param creditorInstitution the creditorInstitution to set
	 */
	public void setCreditorInstitution(String creditorInstitution) {
		this.creditorInstitution = creditorInstitution;
	}

	/**
	 * @return the digitalStamp
	 */
	public boolean isDigitalStamp() {
		return digitalStamp;
	}

	/**
	 * @param digitalStamp the digitalStamp to set
	 */
	public void setDigitalStamp(boolean digitalStamp) {
		this.digitalStamp = digitalStamp;
	}

	/**
	 * @return the tranferCategory
	 */
	public String getTranferCategory() {
		return tranferCategory;
	}

	/**
	 * @param tranferCategory the tranferCategory to set
	 */
	public void setTranferCategory(String tranferCategory) {
		this.tranferCategory = tranferCategory;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TransferForFeeService [creditorInstitution=");
		builder.append(creditorInstitution);
		builder.append(", digitalStamp=");
		builder.append(digitalStamp);
		builder.append(", tranferCategory=");
		builder.append(tranferCategory);
		builder.append("]");
		return builder.toString();
	}
}