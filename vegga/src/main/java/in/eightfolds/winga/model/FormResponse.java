package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormResponse implements Serializable {

	private Long contentId;
	private Long fcId;
	private String formName;
	private String formDescription;
	private String formButtonText;
	private String alert1;
	private String alert2;
	private List<FormDetail> formdetails;

	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public Long getFcId() {
		return fcId;
	}

	public void setFcId(Long fcId) {
		this.fcId = fcId;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}
	
	public String getFormDescription() {
		return formDescription;
	}

	public void setFormDescription(String formDescription) {
		this.formDescription = formDescription;
	}

	public String getFormButtonText() {
		return formButtonText;
	}

	public void setFormButtonText(String formButtonText) {
		this.formButtonText = formButtonText;
	}

	public String getAlert1() {
		return alert1;
	}

	public void setAlert1(String alert1) {
		this.alert1 = alert1;
	}

	public String getAlert2() {
		return alert2;
	}

	public void setAlert2(String alert2) {
		this.alert2 = alert2;
	}

	public List<FormDetail> getFormdetails() {
		return formdetails;
	}

	public void setFormdetails(List<FormDetail> formdetails) {
		this.formdetails = formdetails;
	}

}
