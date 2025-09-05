package in.eightfolds.winga.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class VoucherMainRequest implements Serializable {

    private ArrayList<VoucherRequestItem> voucherRequests;
    private long redeemReqGroupId;
    private long points;

    public ArrayList<VoucherRequestItem> getVoucherRequests() {
        return voucherRequests;
    }

    public void setVoucherRequests(ArrayList<VoucherRequestItem> voucherRequests) {
        this.voucherRequests = voucherRequests;
    }

    public long getRedeemReqGroupId() {
        return redeemReqGroupId;
    }

    public void setRedeemReqGroupId(long redeemReqGroupId) {
        this.redeemReqGroupId = redeemReqGroupId;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }
}
