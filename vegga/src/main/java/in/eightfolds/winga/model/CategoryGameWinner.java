package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class CategoryGameWinner implements Serializable {

    private Long categoryWinnerId;
    private Long cgsId;
    private Long categoryId;
    private Long userId;
    private Long  cgspId;
    private Integer state;
    private String  gameDate;
    private boolean deleted;
    private String createdTime;
    private String modifiedTime;
    private String prizeName;
    private String userName;
    private Long  profilePicId;
    private String title;
    private String desc;
    private String stateName;
    private Integer prizeType;
    private String amount;

    public Long getCategoryWinnerId() {
        return categoryWinnerId;
    }

    public void setCategoryWinnerId(Long categoryWinnerId) {
        this.categoryWinnerId = categoryWinnerId;
    }

    public Long getCgsId() {
        return cgsId;
    }

    public void setCgsId(Long cgsId) {
        this.cgsId = cgsId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCgspId() {
        return cgspId;
    }

    public void setCgspId(Long cgspId) {
        this.cgspId = cgspId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getGameDate() {
        return gameDate;
    }

    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getProfilePicId() {
        return profilePicId;
    }

    public void setProfilePicId(Long profilePicId) {
        this.profilePicId = profilePicId;
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

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Integer getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(Integer prizeType) {
        this.prizeType = prizeType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
