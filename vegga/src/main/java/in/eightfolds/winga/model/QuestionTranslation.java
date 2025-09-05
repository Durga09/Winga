package in.eightfolds.winga.model;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class QuestionTranslation implements Serializable {
	
	protected Long id;
	protected Long qId;
	protected Long langId;
	protected String txt;
	protected Integer dirtyFlag;
	protected Long translatedBy;
	protected Integer approveStatus;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getqId() {
		return qId;
	}
	public void setqId(Long qId) {
		this.qId = qId;
	}
	public Long getLangId() {
		return langId;
	}
	public void setLangId(Long langId) {
		this.langId = langId;
	}
	public String getTxt() {
		return txt;
	}
	public void setTxt(String txt) {
		this.txt = txt;
	}
	
	public Long getTranslatedBy() {
		return translatedBy;
	}
	public void setTranslatedBy(Long translatedBy) {
		this.translatedBy = translatedBy;
	}
	public Integer getApproveStatus() {
		return approveStatus;
	}
	public void setApproveStatus(Integer approveStatus) {
		this.approveStatus = approveStatus;
	}
	public Integer getDirtyFlag() {
		return dirtyFlag;
	}
	public void setDirtyFlag(Integer dirtyFlag) {
		this.dirtyFlag = dirtyFlag;
	}
	@Override
	public String toString() {
		return "QuestionTranslation [id=" + id + ", qId=" + qId + ", langId=" + langId + ", txt=" + txt + ", dirtyFlag="
				+ dirtyFlag + ", translatedBy=" + translatedBy + ", approveStatus=" + approveStatus + "]";
	}
	
}
