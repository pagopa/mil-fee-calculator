package it.gov.pagopa.swclient.mil.feecalculator.bean;

public class FeeResponse {
	private String fee;

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	@Override
	public String toString() {
		return "FeeResponse [fee=" + fee + "]";
	}
}
