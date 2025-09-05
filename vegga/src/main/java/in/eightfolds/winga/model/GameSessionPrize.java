package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by sp on 14/05/18.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class GameSessionPrize  implements Serializable{

    private Long prizeId;
    private Long gSessionId;
    private String title;
    private String desc;
    private Integer type;
    private Double amount;
    private String  url;
    private Integer  totalQty;
    private Integer  prizeQty;
    private String createdTime;
    private String modifiedTime;
    private boolean deleted;
    public Long getPrizeId() {
        return prizeId;
    }
    public void setPrizeId(Long prizeId) {
        this.prizeId = prizeId;
    }
    public Long getgSessionId() {
        return gSessionId;
    }
    public void setgSessionId(Long gSessionId) {
        this.gSessionId = gSessionId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public Integer getTotalQty() {
        return totalQty;
    }
    public void setTotalQty(Integer totalQty) {
        this.totalQty = totalQty;
    }
    public Integer getPrizeQty() {
        return prizeQty;
    }
    public void setPrizeQty(Integer prizeQty) {
        this.prizeQty = prizeQty;
    }
    public String getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
    public String getModifiedTime() {
        return modifiedTime;
    }
    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    @Override
    public String toString() {
        return "GameSessionPrize [prizeId=" + prizeId + ", gSessionId="+ gSessionId + ", title=" + title + ", desc=" + desc
                + ", type=" + type + ", amount=" + amount + ", url=" + url
                + ", totalQty=" + totalQty + ", prizeQty=" + prizeQty + ", createdTime=" + createdTime + ", modifiedTime="
                + modifiedTime + ", deleted=" + deleted + "]";
    }
}
