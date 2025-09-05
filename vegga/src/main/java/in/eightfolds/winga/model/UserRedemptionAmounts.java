package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRedemptionAmounts implements Serializable {
	private double successAmt;
	private double failedAmt;
	private double pendingAmt;

	public double getSuccessAmt() {
		return successAmt;
	}

	public void setSuccessAmt(double successAmt) {
		this.successAmt = successAmt;
	}

	public double getFailedAmt() {
		return failedAmt;
	}

	public void setFailedAmt(double failedAmt) {
		this.failedAmt = failedAmt;
	}

	public double getPendingAmt() {
		return pendingAmt;
	}

	public void setPendingAmt(double pendingAmt) {
		this.pendingAmt = pendingAmt;
	}

	@Override
	public String toString() {
		return "UserRedemptionAmounts [successAmt=" + successAmt + ", failedAmt=" + failedAmt + ", pendingAmt="
				+ pendingAmt + "]";
	}
}
