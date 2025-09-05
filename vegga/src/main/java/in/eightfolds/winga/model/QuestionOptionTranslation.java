package in.eightfolds.winga.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class QuestionOptionTranslation  implements Serializable {
	private Long id;
	private Long oppId;
	private Long langId;
	private String txt;
	private boolean dirtyFlag;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOppId() {
		return oppId;
	}
	public void setOppId(Long oppId) {
		this.oppId = oppId;
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
	public boolean isDirtyFlag() {
		return dirtyFlag;
	}
	public void setDirtyFlag(boolean dirtyFlag) {
		this.dirtyFlag = dirtyFlag;
	}
	@Override
	public String toString() {
		return "QuestionOptionTranslation [id=" + id + ", oppId=" + oppId
				+ ", langId=" + langId + ", txt=" + txt + ", dirtyFlag="
				+ dirtyFlag + "]";
	}
	
	
	
	
	
}
