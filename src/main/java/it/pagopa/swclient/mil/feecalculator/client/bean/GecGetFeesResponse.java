package it.pagopa.swclient.mil.feecalculator.client.bean;

import java.util.List;

public class GecGetFeesResponse {

	/**
	 * 
	 */
	private Boolean belowThreshold;

	/**
	 * 
	 */
	private List<BundleOption> bundleOptions;

	/**
	 * Gets belowThreshold
	 * 
	 * @return value of belowThreshold
	 */
	public Boolean getBelowThreshold() {
		return belowThreshold;
	}

	/**
	 * Sets belowThreshold
	 * 
	 * @param belowThreshold value of belowThreshold
	 */
	public void setBelowThreshold(Boolean belowThreshold) {
		this.belowThreshold = belowThreshold;
	}

	/**
	 * Gets bundleOptions
	 * 
	 * @return value of bundleOptions
	 */
	public List<BundleOption> getBundleOptions() {
		return bundleOptions;
	}

	/**
	 * Sets bundleOptions
	 * 
	 * @param bundleOptions value of bundleOptions
	 */
	public void setBundleOptions(List<BundleOption> bundleOptions) {
		this.bundleOptions = bundleOptions;
	}

	@Override
	public String toString() {
		return new StringBuilder("GecGetFeesResponse [belowThreshold=")
			.append(belowThreshold)
			.append(", bundleOptions=")
			.append(bundleOptions)
			.append("]").toString();
	}

}
