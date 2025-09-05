package in.eightfolds.winga.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;



@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class QuestionResponse extends QuestionTranslation implements Serializable {
	

	private Long contentId;
	private Long categoryId;
	private String langIsoCode;
	private Long selectedOppId;
	private String selectedOppTxt;
	private boolean ansCorrect;
	private String languageName;
	private String title;
	private String comment;
	private String qErr;
	private List<QuestionOptionResponse> options = new ArrayList<QuestionOptionResponse>();
	private List<QuestionTranslation> questionTranslations = new ArrayList<QuestionTranslation>();
	
	public QuestionResponse(){
		
	};
	public QuestionResponse(QuestionResponse question) {
		
		this.contentId = question.getContentId();
		this.categoryId = question.getCategoryId();
		this.langIsoCode = question.getLangIsoCode();
		this.selectedOppId = question.getSelectedOppId() ;
		this.selectedOppTxt = question.getSelectedOppTxt();
		this.ansCorrect = question.isAnsCorrect() ;
		this.options = question.getOptions();
		this.id = question.getId();
		this.qId = question.getqId();
		this.langId = question.getLangId();
		this.txt = question.getTxt();
		
	}
	public Long getContentId() {
		return contentId;
	}
	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getLangIsoCode() {
		return langIsoCode;
	}
	public void setLangIsoCode(String langIsoCode) {
		this.langIsoCode = langIsoCode;
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
	public List<QuestionOptionResponse> getOptions() {
		return options;
	}
	public void setOptions(List<QuestionOptionResponse> options) {
		this.options = options;
	}
	public String getLanguageName() {
		return languageName;
	}
	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<QuestionTranslation> getQuestionTranslations() {
		return questionTranslations;
	}
	public void setQuestionTranslations(List<QuestionTranslation> questionTranslations) {
		this.questionTranslations = questionTranslations;
	}
	@Override
	public String toString() {
		return "QuestionResponse [contentId=" + contentId + ", langIsoCode=" + langIsoCode + ", selectedOppId="
				+ selectedOppId + ", selectedOppTxt=" + selectedOppTxt + ", ansCorrect=" + ansCorrect
				+ ", languageName=" + languageName + ", title=" + title + ", options=" + options
				+ ", questionTranslations=" + questionTranslations + "]";
	}
	public String getqErr() {
		return qErr;
	}
	public void setqErr(String qErr) {
		this.qErr = qErr;
	}


	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
