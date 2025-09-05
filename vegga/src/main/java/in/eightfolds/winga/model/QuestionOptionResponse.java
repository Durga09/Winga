package in.eightfolds.winga.model;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class QuestionOptionResponse extends QuestionOptionTranslation implements Serializable {

	 private String langIsoCode;
	 private Long qId;
	 private Long contentId;
	 private boolean correct;
	 private String title;
	 private String optErr;
	 private List<QuestionOptionTranslation> questionOptionTranslations = new ArrayList<QuestionOptionTranslation>();
	 
	public String getLangIsoCode() {
		return langIsoCode;
	}
	public void setLangIsoCode(String langIsoCode) {
		this.langIsoCode = langIsoCode;
	}
	public Long getqId() {
		return qId;
	}
	public void setqId(Long qId) {
		this.qId = qId;
	}
	
	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}
	
	
	public Long getContentId() {
		return contentId;
	}
	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<QuestionOptionTranslation> getQuestionOptionTranslations() {
		return questionOptionTranslations;
	}
	public void setQuestionOptionTranslations(List<QuestionOptionTranslation> questionOptionTranslations) {
		this.questionOptionTranslations = questionOptionTranslations;
	}
	@Override
	 public boolean equals(Object obj) {
	 if(obj instanceof QuestionOptionTranslation){
		 QuestionOptionTranslation userTmp = (QuestionOptionTranslation) obj;
	    if(userTmp.getOppId() != null && this.getOppId() !=null && userTmp.getOppId().longValue() == this.getOppId()){
	      return true;
	    }
	 }
	      return super.equals(obj);
	 }
	
	@Override
	public String toString() {
		return "QuestionOptionResponse [langIsoCode=" + langIsoCode + ", qId=" + qId + ", contentId=" + contentId
				+ ", correct=" + correct + ", title=" + title + ", questionOptionTranslations="
				+ questionOptionTranslations + "]";
	}
	public String getOptErr() {
		return optErr;
	}
	public void setOptErr(String optErr) {
		this.optErr = optErr;
	}
	
	
}
