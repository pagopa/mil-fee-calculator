package it.pagopa.swclient.mil.feecalculator.client.bean;

/**
 *
 */
public class AcquirerConfiguration {

	/**
	 * The psp configuration to be used when calling the "verify" and "activate" APIs of the node
	 */
	private PspConfiguration pspConfigForVerifyAndActivate;

	/**
	 * The psp configuration to be used when calling the "close" API of the node and the "getFee" api of
	 * GEC
	 */
	private PspConfiguration pspConfigForGetFeeAndClosePayment;

	/**
	 * Gets pspConfigForVerifyAndActivate
	 *
	 * @return value of pspConfigForVerifyAndActivate
	 */
	public PspConfiguration getPspConfigForVerifyAndActivate() {
		return pspConfigForVerifyAndActivate;
	}

	/**
	 * Sets pspConfigForVerifyAndActivate
	 *
	 * @param pspConfigForVerifyAndActivate value of pspConfigForVerifyAndActivate
	 */
	public void setPspConfigForVerifyAndActivate(PspConfiguration pspConfigForVerifyAndActivate) {
		this.pspConfigForVerifyAndActivate = pspConfigForVerifyAndActivate;
	}

	/**
	 * Gets pspConfigForGetFeeAndClosePayment
	 *
	 * @return value of pspConfigForGetFeeAndClosePayment
	 */
	public PspConfiguration getPspConfigForGetFeeAndClosePayment() {
		return pspConfigForGetFeeAndClosePayment;
	}

	/**
	 * Sets pspConfigForGetFeeAndClosePayment
	 *
	 * @param pspConfigForGetFeeAndClosePayment value of pspConfigForGetFeeAndClosePayment
	 */
	public void setPspConfigForGetFeeAndClosePayment(PspConfiguration pspConfigForGetFeeAndClosePayment) {
		this.pspConfigForGetFeeAndClosePayment = pspConfigForGetFeeAndClosePayment;
	}

	@Override
	public String toString() {
		return new StringBuilder("AcquirerConfiguration [pspConfigForVerifyAndActivate=")
			.append(pspConfigForVerifyAndActivate)
			.append(", pspConfigForGetFeeAndClosePayment=")
			.append(pspConfigForGetFeeAndClosePayment)
			.append("]").toString();
	}
}
