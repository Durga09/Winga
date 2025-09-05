package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Form implements Serializable {

    private int contentId;
    private int fcId;

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

    private String alert1;

    public String getFormButtonText() {
        return formButtonText;
    }

    public void setFormButtonText(String formButtonText) {
        this.formButtonText = formButtonText;
    }

    private String formButtonText;

    public String getFormDescription() {
        return formDescription;
    }

    public void setFormDescription(String formDescription) {
        this.formDescription = formDescription;
    }

    private String formDescription;
    private String alert2;

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public int getFcId() {
        return fcId;
    }

    public void setFcId(int fcId) {
        this.fcId = fcId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public List<FormDetails> getFormdetails() {
        return formdetails;
    }

    public void setFormdetails(List<FormDetails> formdetails) {
        this.formdetails = formdetails;
    }

    private String formName;
    private List<FormDetails> formdetails;
}