package in.eightfolds.winga.v2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpinResponse implements Serializable {

    public String spinButtonText;
    public String spinTermsAndConditions;
    public ArrayList<SpinItem> spinItems;
    public Integer resultSpinItemId;
    public String number;
    public String resultSpinItemNumber;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSpinButtonText() {
        return spinButtonText;
    }

    public void setSpinButtonText(String spinButtonText) {
        this.spinButtonText = spinButtonText;
    }

    public String getSpinTermsAndConditions() {
        return spinTermsAndConditions;
    }

    public void setSpinTermsAndConditions(String spinTermsAndConditions) {
        this.spinTermsAndConditions = spinTermsAndConditions;
    }

    public ArrayList<SpinItem> getSpinItems() {
        return spinItems;
    }

    public void setSpinItems(ArrayList<SpinItem> spinItems) {
        this.spinItems = spinItems;
    }

    public Integer getResultSpinItemId() {
        return resultSpinItemId;
    }

    public void setResultSpinItemId(Integer resultSpinItemId) {
        this.resultSpinItemId = resultSpinItemId;
    }

    public String getResultSpinItemNumber() {
        return resultSpinItemNumber;
    }

    public void setResultSpinItemNumber(String resultSpinItemNumber) {
        this.resultSpinItemNumber = resultSpinItemNumber;
    }
}
