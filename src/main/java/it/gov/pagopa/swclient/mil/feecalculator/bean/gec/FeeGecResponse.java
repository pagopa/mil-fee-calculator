package it.gov.pagopa.swclient.mil.feecalculator.bean.gec;

public class FeeGecResponse {
	/*
	 * 
	 */
	private String bundleDescription;
	
	/*
	 * 
	 */
	private String bundleName;
	
	/*
	 * 
	 */
	private String idBrokerPsp;
	
	/*
	 * 
	 */
	private String idBundle;
	
	/*
	 * 
	 */
	private String idChannel;
	
	/*
	 * 
	 */
	private String idCiBundle;
	
	/*
	 * 
	 */
	private String idPsp;
	
	/*
	 * 
	 */
	private boolean onUs;
	
	/*
	 * 
	 */
	private String paymentMethod;
	
	/*
	 * 
	 */
	private long primaryCiIncurredFee;
	
	/*
	 * 
	 */
	private long taxPayerFee;
	
	/*
	 * 
	 */
	private String touchpoint;
	
	/**
	 * @return the bundleDescription
	 */
	public String getBundleDescription() {
		return bundleDescription;
	}

	/**
	 * @param bundleDescription the bundleDescription to set
	 */
	public void setBundleDescription(String bundleDescription) {
		this.bundleDescription = bundleDescription;
	}

	/**
	 * @return the bundleName
	 */
	public String getBundleName() {
		return bundleName;
	}

	/**
	 * @param bundleName the bundleName to set
	 */
	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}

	/**
	 * @return the idBrokerPsp
	 */
	public String getIdBrokerPsp() {
		return idBrokerPsp;
	}

	/**
	 * @param idBrokerPsp the idBrokerPsp to set
	 */
	public void setIdBrokerPsp(String idBrokerPsp) {
		this.idBrokerPsp = idBrokerPsp;
	}

	/**
	 * @return the idBundle
	 */
	public String getIdBundle() {
		return idBundle;
	}

	/**
	 * @param idBundle the idBundle to set
	 */
	public void setIdBundle(String idBundle) {
		this.idBundle = idBundle;
	}

	/**
	 * @return the idChannel
	 */
	public String getIdChannel() {
		return idChannel;
	}

	/**
	 * @param idChannel the idChannel to set
	 */
	public void setIdChannel(String idChannel) {
		this.idChannel = idChannel;
	}

	/**
	 * @return the idCiBundle
	 */
	public String getIdCiBundle() {
		return idCiBundle;
	}

	/**
	 * @param idCiBundle the idCiBundle to set
	 */
	public void setIdCiBundle(String idCiBundle) {
		this.idCiBundle = idCiBundle;
	}

	/**
	 * @return the idPsp
	 */
	public String getIdPsp() {
		return idPsp;
	}

	/**
	 * @param idPsp the idPsp to set
	 */
	public void setIdPsp(String idPsp) {
		this.idPsp = idPsp;
	}

	/**
	 * @return the onUs
	 */
	public boolean isOnUs() {
		return onUs;
	}

	/**
	 * @param onUs the onUs to set
	 */
	public void setOnUs(boolean onUs) {
		this.onUs = onUs;
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
	 * @return the primaryCiIncurredFee
	 */
	public long getPrimaryCiIncurredFee() {
		return primaryCiIncurredFee;
	}

	/**
	 * @param primaryCiIncurredFee the primaryCiIncurredFee to set
	 */
	public void setPrimaryCiIncurredFee(long primaryCiIncurredFee) {
		this.primaryCiIncurredFee = primaryCiIncurredFee;
	}

	/**
	 * @return the taxPayerFee
	 */
	public long getTaxPayerFee() {
		return taxPayerFee;
	}

	/**
	 * @param taxPayerFee the taxPayerFee to set
	 */
	public void setTaxPayerFee(long taxPayerFee) {
		this.taxPayerFee = taxPayerFee;
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
