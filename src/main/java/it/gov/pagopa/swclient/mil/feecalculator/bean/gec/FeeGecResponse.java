package it.gov.pagopa.swclient.mil.feecalculator.bean.gec;

public class FeeGecResponse {
	private String bundleDescription;
	
	private String bundleName;
	
	private String idBrokerPsp;
	
	private String idBundle;
	
	private String idChannel;
	
	private String idCiBundle;
	
	private String idPsp;
	
	private boolean onUs;
	
	private String paymentMethod;
	
	private long primaryCiIncurredFee;
	
	private long taxPayerFee;
	
	private String touchpoint;
	
	public String getBundleDescription() {
		return bundleDescription;
	}
	
	public void setBundleDescription(String bundleDescription) {
		this.bundleDescription = bundleDescription;
	}
	
	public String getBundleName() {
		return bundleName;
	}
	
	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}
	
	public String getIdBrokerPsp() {
		return idBrokerPsp;
	}
	
	public void setIdBrokerPsp(String idBrokerPsp) {
		this.idBrokerPsp = idBrokerPsp;
	}
	
	public String getIdBundle() {
		return idBundle;
	}
	
	public void setIdBundle(String idBundle) {
		this.idBundle = idBundle;
	}
	
	public String getIdChannel() {
		return idChannel;
	}
	
	public void setIdChannel(String idChannel) {
		this.idChannel = idChannel;
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
	
	public boolean isOnUs() {
		return onUs;
	}
	
	public void setOnUs(boolean onUs) {
		this.onUs = onUs;
	}
	
	public String getPaymentMethod() {
		return paymentMethod;
	}
	
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
	public long getPrimaryCiIncurredFee() {
		return primaryCiIncurredFee;
	}
	
	public void setPrimaryCiIncurredFee(long primaryCiIncurredFee) {
		this.primaryCiIncurredFee = primaryCiIncurredFee;
	}
	
	public long getTaxPayerFee() {
		return taxPayerFee;
	}
	
	public void setTaxPayerFee(long taxPayerFee) {
		this.taxPayerFee = taxPayerFee;
	}
	
	public String getTouchpoint() {
		return touchpoint;
	}
	
	public void setTouchpoint(String touchpoint) {
		this.touchpoint = touchpoint;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FeeServiceResponse [bundleDescription=");
		builder.append(bundleDescription);
		builder.append(", bundleName=");
		builder.append(bundleName);
		builder.append(", idBrokerPsp=");
		builder.append(idBrokerPsp);
		builder.append(", idBundle=");
		builder.append(idBundle);
		builder.append(", idChannel=");
		builder.append(idChannel);
		builder.append(", idCiBundle=");
		builder.append(idCiBundle);
		builder.append(", idPsp=");
		builder.append(idPsp);
		builder.append(", onUs=");
		builder.append(onUs);
		builder.append(", paymentMethod=");
		builder.append(paymentMethod);
		builder.append(", primaryCiIncurredFee=");
		builder.append(primaryCiIncurredFee);
		builder.append(", taxPayerFee=");
		builder.append(taxPayerFee);
		builder.append(", touchpoint=");
		builder.append(touchpoint);
		builder.append("]");
		return builder.toString();
	}
}
