package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by sp on 14/05/18.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class QuestionOption implements Serializable {

    private long oppId;
    private Long qId;
    private boolean correct;
    private boolean deleted;
    private String createdTime;
    private String modifiedTime;

    private String txt;

    public long getOppId() {
        return oppId;
    }

    public void setOppId(long oppId) {
        this.oppId = oppId;
    }

    public Long getqId() {
        return qId;
    }

    public void setqId(Long qId) {
        this.qId = qId;
    }

    public boolean getCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
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

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    @Override
    public String toString() {
        return "QuestionOption [oppId=" + oppId + ", qId=" + qId
                + ", correct=" + correct + ", deleted=" + deleted
                + ", createdTime=" + createdTime + ", modifiedTime="
                + modifiedTime + ", txt=" + txt + "]";
    }


}
