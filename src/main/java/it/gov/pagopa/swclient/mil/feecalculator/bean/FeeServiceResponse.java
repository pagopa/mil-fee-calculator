package it.gov.pagopa.swclient.mil.feecalculator.bean;

public class FeeServiceResponse {
	private long taxPayerFee;
	private long primaryCiIncurredFee;
	private String paymentMethod;
	private String touchpoint;
	private String idBundle;
	private String bundleName;
	private String bundleDescription;
	private String idCiBundle;
	private String idPsp;
	public long getTaxPayerFee() {
		return taxPayerFee;
	}
	public void setTaxPayerFee(long taxPayerFee) {
		this.taxPayerFee = taxPayerFee;
	}
	public long getPrimaryCiIncurredFee() {
		return primaryCiIncurredFee;
	}
	public void setPrimaryCiIncurredFee(long primaryCiIncurredFee) {
		this.primaryCiIncurredFee = primaryCiIncurredFee;
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
	public String getIdBundle() {
		return idBundle;
	}
	public void setIdBundle(String idBundle) {
		this.idBundle = idBundle;
	}
	public String getBundleName() {
		return bundleName;
	}
	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}
	public String getBundleDescription() {
		return bundleDescription;
	}
	public void setBundleDescription(String bundleDescription) {
		this.bundleDescription = bundleDescription;
	}
	public String getIdCiBundle() {
		return idCiBundle;
	}
	public void setIdCiBundle(String idCiBundle) {
		this.idCiBundle = idCiBundle;
	}
	public String getIdPsp() {
		return idPsp;
	}
	public void setIdPsp(String idPsp) {
		this.idPsp = idPsp;
	}
	@Override
	public String toString() {
		return "FeeServiceResponse [taxPayerFee=" + taxPayerFee + ", primaryCiIncurredFee=" + primaryCiIncurredFee
				+ ", paymentMethod=" + paymentMethod + ", touchpoint=" + touchpoint + ", idBundle=" + idBundle
				+ ", bundleName=" + bundleName + ", bundleDescription=" + bundleDescription + ", idCiBundle="
				+ idCiBundle + ", idPsp=" + idPsp + "]";
	}
	
}
