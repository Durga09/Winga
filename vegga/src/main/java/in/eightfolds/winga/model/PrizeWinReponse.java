package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class PrizeWinReponse implements Serializable {

    private Float totalRewardsAmt;
    private RedemptionAmounts redemptionAmounts;
    private List<PrizeWin> prizeWins;
    private Integer totalRecord;
    private Integer pageSize;

    public Float getTotalRewardsAmt() {
        return totalRewardsAmt;
    }
    public void setTotalRewardsAmt(Float totalRewardsAmt) {
        this.totalRewardsAmt = totalRewardsAmt;
    }
    public RedemptionAmounts getRedemptionAmounts() {
        return redemptionAmounts;
    }
    public void setRedemptionAmounts(RedemptionAmounts redemptionAmounts) {
        this.redemptionAmounts = redemptionAmounts;
    }
    public List<PrizeWin> getPrizeWins() {
        return prizeWins;
    }
    public void setPrizeWins(List<PrizeWin> prizeWins) {
        this.prizeWins = prizeWins;
    }
    public Integer getTotalRecord() {
        return totalRecord;
    }
    public void setTotalRecord(Integer totalRecord) {
        this.totalRecord = totalRecord;
    }
    public Integer getPageSize() {
        return pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    @Override
    public String toString() {
        return "PrizeWinReponse [totalRewardsAmt=" + totalRewardsAmt
                + ", redemptionAmounts=" + redemptionAmounts + ", prizeWins="
                + prizeWins + ", totalRecord=" + totalRecord + ", pageSize="
                + pageSize + "]";
    }

}
