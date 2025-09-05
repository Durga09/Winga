package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoyalityPointHistoryResponse implements Serializable {

    private List<LoyalityPointHistory> loyalityPointHistories;
    private Double totalLoyalityPoints;
    private Double redeemedLoyalityPoints;


    public List<LoyalityPointHistory> getLoyalityPointHistories() {
        return loyalityPointHistories;
    }

    public void setLoyalityPointHistories(List<LoyalityPointHistory> loyalityPointHistories) {
        this.loyalityPointHistories = loyalityPointHistories;
    }

    public Double getTotalLoyalityPoints() {
        return totalLoyalityPoints;
    }

    public void setTotalLoyalityPoints(Double totalLoyalityPoints) {
        this.totalLoyalityPoints = totalLoyalityPoints;
    }

    public Double getRedeemedLoyalityPoints() {
        return redeemedLoyalityPoints;
    }

    public void setRedeemedLoyalityPoints(Double redeemedLoyalityPoints) {
        this.redeemedLoyalityPoints = redeemedLoyalityPoints;
    }
}
