package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class NewGameSubmitRequest extends CategoryGame implements Serializable {

    List<QuestionResponse> questions;
    List<ContentViewDetail> contentViewDetails;
    FormResponse form;

    public FormResponse getForm() {
        return form;
    }

    public void setForm(FormResponse form) {
        this.form = form;
    }

    public List<ContentViewDetail> getContentViewDetails() {
        return contentViewDetails;
    }

    public void setContentViewDetails(List<ContentViewDetail> contentViewDetails) {
        this.contentViewDetails = contentViewDetails;
    }

    public List<QuestionResponse> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionResponse> questions) {
        this.questions = questions;
    }
}
