package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;



import java.io.Serializable;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class ContentResponse extends ContentTranslation implements Serializable {

	@JsonIgnore
	private int currentQuestion;
	private BuyLinkData buyLinkData;
	private ShareLinkData shareLinkData;
	private Long bannerDuration;
	private String langIsoCode;
	private List<QuestionResponse> questions;
	private String ansQuestionBtnTxt;

	public BuyLinkData getBuyLinkData() {
		return buyLinkData;
	}

	public void setBuyLinkData(BuyLinkData buyLinkData) {
		this.buyLinkData = buyLinkData;
	}

	public String getLangIsoCode() {
		return langIsoCode;
	}

	public void setLangIsoCode(String langIsoCode) {
		this.langIsoCode = langIsoCode;
	}

	public List<QuestionResponse> getQuestions() {
		return questions;
	}

	public int getCurrentQuestion() {
		return currentQuestion;
	}

	public void setCurrentQuestion(int currentQuestion) {
		this.currentQuestion = currentQuestion;
	}

	public void setQuestions(List<QuestionResponse> questions) {
		this.questions = questions;
	}

	public Long getBannerDuration() {
		return bannerDuration;
	}

	public void setBannerDuration(Long bannerDuration) {
		this.bannerDuration = bannerDuration;
	}

	public ShareLinkData getShareLinkData() {
		return shareLinkData;
	}

	public void setShareLinkData(ShareLinkData shareLinkData) {
		this.shareLinkData = shareLinkData;
	}

	public String getAnsQuestionBtnTxt() {
		return ansQuestionBtnTxt;
	}

	public void setAnsQuestionBtnTxt(String ansQuestionBtnTxt) {
		this.ansQuestionBtnTxt = ansQuestionBtnTxt;
	}

	@Override
	public String toString() {
		return "ContentResponse [langIsoCode=" + langIsoCode + ", questions="
				+ questions + "]";
	}


}
