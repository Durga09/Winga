package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RedemptionAmounts implements Serializable {

    protected float successAmt;
    protected float failedAmt;
    protected float pendingAmt;

    public float getSuccessAmt() {
        return successAmt;
    }

    public void setSuccessAmt(float successAmt) {
        this.successAmt = successAmt;
    }

    public float getFailedAmt() {
        return failedAmt;
    }

    public void setFailedAmt(float failedAmt) {
        this.failedAmt = failedAmt;
    }

    public float getPendingAmt() {
        return pendingAmt;
    }

    public void setPendingAmt(float pendingAmt) {
        this.pendingAmt = pendingAmt;
    }
}
