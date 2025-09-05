package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class AddressType implements Serializable{
    private Long adreTypeId;
    private String title;
    private Long deleted;
    private String createdTime;
    private String modifiedTime;

    public Long getAdreTypeId() {
        return adreTypeId;
    }

    public void setAdreTypeId(Long adreTypeId) {
        this.adreTypeId = adreTypeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getDeleted() {
        return deleted;
    }

    public void setDeleted(Long deleted) {
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

    @Override
    public String toString() {
        return "AddressType [adreTypeId=" + adreTypeId + ", title=" + title
                + ", deleted=" + deleted + ", createdTime=" + createdTime
                + ", modifiedTime=" + modifiedTime + "]";
    }


}
