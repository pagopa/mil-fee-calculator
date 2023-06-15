/**
 * 
 */
package it.pagopa.swclient.mil.feecalculator.client.bean;


public class Psp {
	/**
	 * Identifier of the PSP
	 */
	private String idPsp;

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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Psp [idPsp=");
		builder.append(idPsp);
		builder.append("]");
		return builder.toString();
	}
	
}
