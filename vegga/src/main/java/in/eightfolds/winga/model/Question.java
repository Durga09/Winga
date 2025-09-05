package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sp on 14/05/18.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Question implements Serializable {

    private Long qId;
    private Long contentId;
    private boolean deleted;
    private String createdTime;
    private String modifiedTime;
    private String txt;
    private Long selectedOppId;
    private String selectedOppTxt;
    private boolean ansCorrect;

    private List<QuestionOption> options;

    public Long getqId() {
        return qId;
    }

    public void setqId(Long qId) {
        this.qId = qId;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
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

    public List<QuestionOption> getOptions() {
        return options;
    }

    public void setOptions(List<QuestionOption> options) {
        this.options = options;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public Long getSelectedOppId() {
        return selectedOppId;
    }

    public void setSelectedOppId(Long selectedOppId) {
        this.selectedOppId = selectedOppId;
    }

    public String getSelectedOppTxt() {
        return selectedOppTxt;
    }

    public void setSelectedOppTxt(String selectedOppTxt) {
        this.selectedOppTxt = selectedOppTxt;
    }

    public boolean isAnsCorrect() {
        return ansCorrect;
    }

    public void setAnsCorrect(boolean ansCorrect) {
        this.ansCorrect = ansCorrect;
    }

    @Override
    public String toString() {
        return "Question [qId=" + qId + ", contentId=" + contentId
                + ", deleted=" + deleted + ", createdTime=" + createdTime
                + ", modifiedTime=" + modifiedTime + ", txt=" + txt
                + ", selectedOppId=" + selectedOppId + ", selectedOppTxt="
                + selectedOppTxt + ", ansCorrect=" + ansCorrect
                + ", options=" + options + "]";
    }


}