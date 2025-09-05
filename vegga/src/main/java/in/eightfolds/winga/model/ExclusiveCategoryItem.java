package in.eightfolds.winga.model;

import java.io.Serializable;

public class ExclusiveCategoryItem implements Serializable {

    private Long eciId;
    private Integer ecId;
    private Long referedUserId;
    private String title;
    private String message;
    private Long imageId;
    private String imageUrl;
    private String redirectUrl;
    private String createdTime;

    public Long getEciId() {
        return eciId;
    }

    public void setEciId(Long eciId) {
        this.eciId = eciId;
    }

    public Integer getEcId() {
        return ecId;
    }

    public void setEcId(Integer ecId) {
        this.ecId = ecId;
    }

    public Long getReferedUserId() {
        return referedUserId;
    }

    public void setReferedUserId(Long referedUserId) {
        this.referedUserId = referedUserId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
}
