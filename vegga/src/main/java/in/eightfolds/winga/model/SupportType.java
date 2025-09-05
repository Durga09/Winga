package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class SupportType  implements Serializable{

    private Long  supportTypeId;
    private String  title;
    private boolean  deleted;
    private String  createdTime;
    private String modfiedTime;

    public Long getSupportTypeId() {
        return supportTypeId;
    }
    public void setSupportTypeId(Long supportTypeId) {
        this.supportTypeId = supportTypeId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
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
    public String getModfiedTime() {
        return modfiedTime;
    }
    public void setModfiedTime(String modfiedTime) {
        this.modfiedTime = modfiedTime;
    }
    @Override
    public String toString() {
        return "SupportType [supportTypeId=" + supportTypeId + ", title="
                + title + ", deleted=" + deleted + ", createdTime="
                + createdTime + ", modfiedTime=" + modfiedTime + "]";
    }
}
