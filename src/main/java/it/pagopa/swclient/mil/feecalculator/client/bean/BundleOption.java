package it.pagopa.swclient.mil.feecalculator.client.bean;

public class BundleOption {

	/**
	 *
	 */
	private String abi;

	/**
	 *
	 */
	private String bundleDescription;

	/**
	 *
	 */
	private String bundleName;

	/**
	 *
	 */
	private String idBrokerPsp;

	/**
	 *
	 */
	private String idBundle;

	/**
	 *
	 */
	private String idChannel;

	/**
	 *
	 */
	private String idCiBundle;

	/**
	 *
	 */
	private String idPsp;

	/**
	 *
	 */
	private Boolean onUs;

	/**
	 *
	 */
	private String paymentMethod;

	/**
	 *
	 */
	private long primaryCiIncurredFee;

	/**
	 *
	 */
	private long taxPayerFee;

	/**
	 *
	 */
	private String touchpoint;

	/**
	 * Gets abi
	 * @return value of abi
	 */
	public String getAbi() {
		return abi;
	}

	/**
	 * Sets abi
	 * @param abi value of abi
	 */
	public void setAbi(String abi) {
		this.abi = abi;
	}

	/**
	 * Gets bundleDescription
	 * @return value of bundleDescription
	 */
	public String getBundleDescription() {
		return bundleDescription;
	}

	/**
	 * Sets bundleDescription
	 * @param bundleDescription value of bundleDescription
	 */
	public void setBundleDescription(String bundleDescription) {
		this.bundleDescription = bundleDescription;
	}

	/**
	 * Gets bundleName
	 * @return value of bundleName
	 */
	public String getBundleName() {
		return bundleName;
	}

	/**
	 * Sets bundleName
	 * @param bundleName value of bundleName
	 */
	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}

	/**
	 * Gets idBrokerPsp
	 * @return value of idBrokerPsp
	 */
	public String getIdBrokerPsp() {
		return idBrokerPsp;
	}

	/**
	 * Sets idBrokerPsp
	 * @param idBrokerPsp value of idBrokerPsp
	 */
	public void setIdBrokerPsp(String idBrokerPsp) {
		this.idBrokerPsp = idBrokerPsp;
	}

	/**
	 * Gets idBundle
	 * @return value of idBundle
	 */
	public String getIdBundle() {
		return idBundle;
	}

	/**
	 * Sets idBundle
	 * @param idBundle value of idBundle
	 */
	public void setIdBundle(String idBundle) {
		this.idBundle = idBundle;
	}

	/**
	 * Gets idChannel
	 * @return value of idChannel
	 */
	public String getIdChannel() {
		return idChannel;
	}

	/**
	 * Sets idChannel
	 * @param idChannel value of idChannel
	 */
	public void setIdChannel(String idChannel) {
		this.idChannel = idChannel;
	}

	/**
	 * Gets idCiBundle
	 * @return value of idCiBundle
	 */
	public String getIdCiBundle() {
		return idCiBundle;
	}

	/**
	 * Sets idCiBundle
	 * @param idCiBundle value of idCiBundle
	 */
	public void setIdCiBundle(String idCiBundle) {
		this.idCiBundle = idCiBundle;
	}

	/**
	 * Gets idPsp
	 * @return value of idPsp
	 */
	public String getIdPsp() {
		return idPsp;
	}

	/**
	 * Sets idPsp
	 * @param idPsp value of idPsp
	 */
	public void setIdPsp(String idPsp) {
		this.idPsp = idPsp;
	}

	/**
	 * Gets onUs
	 * @return value of onUs
	 */
	public Boolean getOnUs() {
		return onUs;
	}

	/**
	 * Sets onUs
	 * @param onUs value of onUs
	 */
	public void setOnUs(Boolean onUs) {
		this.onUs = onUs;
	}

	/**
	 * Gets paymentMethod
	 * @return value of paymentMethod
	 */
	public String getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * Sets paymentMethod
	 * @param paymentMethod value of paymentMethod
	 */
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	/**
	 * Gets primaryCiIncurredFee
	 * @return value of primaryCiIncurredFee
	 */
	public long getPrimaryCiIncurredFee() {
		return primaryCiIncurredFee;
	}

	/**
	 * Sets primaryCiIncurredFee
	 * @param primaryCiIncurredFee value of primaryCiIncurredFee
	 */
	public void setPrimaryCiIncurredFee(long primaryCiIncurredFee) {
		this.primaryCiIncurredFee = primaryCiIncurredFee;
	}

	/**
	 * Gets taxPayerFee
	 * @return value of taxPayerFee
	 */
	public long getTaxPayerFee() {
		return taxPayerFee;
	}

	/**
	 * Sets taxPayerFee
	 * @param taxPayerFee value of taxPayerFee
	 */
	public void setTaxPayerFee(long taxPayerFee) {
		this.taxPayerFee = taxPayerFee;
	}

	/**
	 * Gets touchpoint
	 * @return value of touchpoint
	 */
	public String getTouchpoint() {
		return touchpoint;
	}

	/**
	 * Sets touchpoint
	 * @param touchpoint value of touchpoint
	 */
	public void setTouchpoint(String touchpoint) {
		this.touchpoint = touchpoint;
	}

	@Override
	public String toString() {
		return new StringBuilder("BundleOption [abi=")
				.append(abi)
				.append(", bundleDescription=")
				.append(bundleDescription)
				.append(", bundleName=")
				.append(bundleName)
				.append(", idBrokerPsp=")
				.append(idBrokerPsp)
				.append(", idBundle=")
				.append(idBundle)
				.append(", idChannel=")
				.append(idChannel)
				.append(", idCiBundle=")
				.append(idCiBundle)
				.append(", idPsp=")
				.append(idPsp)
				.append(", onUs=")
				.append(onUs)
				.append(", paymentMethod=")
				.append(paymentMethod)
				.append(", primaryCiIncurredFee=")
				.append(primaryCiIncurredFee)
				.append(", taxPayerFee=")
				.append(taxPayerFee)
				.append(", touchpoint=")
				.append(touchpoint)
				.append("]").toString();
	}
}
