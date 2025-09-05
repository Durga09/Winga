package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SurveyResult implements Serializable {
    private Long contentId;
    private Long optionId;
    private Long questionId;
    private String questionTxt;
    private String optionTxt;

    @JsonIgnore
    private Long totalNumberOfUsers;

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getQuestionTxt() {
        return questionTxt;
    }

    public void setQuestionTxt(String questionTxt) {
        this.questionTxt = questionTxt;
    }

    public String getOptionTxt() {
        return optionTxt;
    }

    public void setOptionTxt(String optionTxt) {
        this.optionTxt = optionTxt;
    }

    public Long getUserCount() {
        return userCount;
    }

    public int getUserPercentage(){
        return  Math.round((((float)userCount/(float)totalNumberOfUsers)*100));
    }

    public void setUserCount(Long userCount) {
        this.userCount = userCount;
    }

    private Long userCount;

    public Long getTotalNumberOfUsers() {
        return totalNumberOfUsers;
    }

    public void setTotalNumberOfUsers(Long totalNumberOfUsers) {
        this.totalNumberOfUsers = totalNumberOfUsers;
    }
}
