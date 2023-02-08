package it.gov.pagopa.swclient.mil.feecalculator.bean;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class FeeResponse {
	@NotNull
	@Min(value = 1)
	@Max(value = 99999999999L)
	private Long fee;

	/**
	 * @return the fee
	 */
	public Long getFee() {
		return fee;
	}

	/**
	 * @param fee the fee to set
	 */
	public void setFee(Long fee) {
		this.fee = fee;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FeeResponse [fee=");
		builder.append(fee);
		builder.append("]");
		return builder.toString();
	}
}
