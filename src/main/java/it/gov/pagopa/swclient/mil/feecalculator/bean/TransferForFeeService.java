package it.gov.pagopa.swclient.mil.feecalculator.bean;

public class TransferForFeeService {
	private String creditorInstitution;

	private boolean digitalStamp;
	
	private String tranferCategory;
	
	public String getCreditorInstitution() {
		return creditorInstitution;
	}
	
	public void setCreditorInstitution(String creditorInstitution) {
		this.creditorInstitution = creditorInstitution;
	}
	
	public boolean isDigitalStamp() {
		return digitalStamp;
	}
	
	public void setDigitalStamp(boolean digitalStamp) {
		this.digitalStamp = digitalStamp;
	}
	
	public String getTranferCategory() {
		return tranferCategory;
	}
	
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