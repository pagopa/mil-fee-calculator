package it.gov.pagopa.swclient.mil.feecalculator.dao;

public class PspConfiguration {
	
	/*
	 * 
	 */
	private String pspId;
	
	/*
	 * 
	 */
	private String pspBroker;
	
	/*
	 * 
	 */
	private String pspPassword;

	/**
	 * @return the pspId
	 */
	public String getPspId() {
		return pspId;
	}

	/**
	 * @param pspId the pspId to set
	 */
	public void setPspId(String pspId) {
		this.pspId = pspId;
	}

	/**
	 * @return the pspBroker
	 */
	public String getPspBroker() {
		return pspBroker;
	}

	/**
	 * @param pspBroker the pspBroker to set
	 */
	public void setPspBroker(String pspBroker) {
		this.pspBroker = pspBroker;
	}

	/**
	 * @return the pspPassword
	 */
	public String getPspPassword() {
		return pspPassword;
	}

	/**
	 * @param pspPassword the pspPassword to set
	 */
	public void setPspPassword(String pspPassword) {
		this.pspPassword = pspPassword;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("PspConf{");
		sb.append("pspId='").append(pspId).append('\'');
		sb.append(", pspBroker='").append(pspBroker).append('\'');
		sb.append(", pspPassword='").append(pspPassword).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
