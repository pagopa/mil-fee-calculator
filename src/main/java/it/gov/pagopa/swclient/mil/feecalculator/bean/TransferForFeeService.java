package it.gov.pagopa.swclient.mil.feecalculator.bean;

public class TransferForFeeService {
	private String creditorInstitution;
	private String tranferCategory;
	public String getCreditorInstitution() {
		return creditorInstitution;
	}
	public void setCreditorInstitution(String creditorInstitution) {
		this.creditorInstitution = creditorInstitution;
	}
	public String getTranferCategory() {
		return tranferCategory;
	}
	public void setTranferCategory(String tranferCategory) {
		this.tranferCategory = tranferCategory;
	}
	@Override
	public String toString() {
		return "TransferForFeeService [creditorInstitution=" + creditorInstitution + ", tranferCategory="
				+ tranferCategory + "]";
	} 
}
