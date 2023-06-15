package it.pagopa.swclient.mil.feecalculator.bean;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Response of the getFee API
 */
@RegisterForReflection
public class GetFeeResponse {

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
		builder.append("GetFeeResponse [fee=");
		builder.append(fee);
		builder.append("]");
		return builder.toString();
	}
}
