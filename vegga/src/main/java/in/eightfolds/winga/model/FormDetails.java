package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormDetails  implements Serializable {

    private int fclId;
    private int fftId;
    private String lableName;
    private String lableValue;
    private String defaultValue;
    private boolean editable;

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    private String regex;

    public int getFclId() {
        return fclId;
    }

    public void setFclId(int fclId) {
        this.fclId = fclId;
    }

    public int getFftId() {
        return fftId;
    }

    public void setFftId(int fftId) {
        this.fftId = fftId;
    }

    public String getLableName() {
        return lableName;
    }

    public void setLableName(String lableName) {
        this.lableName = lableName;
    }

    public String getLableValue() {
        return lableValue;
    }

    public void setLableValue(String lableValue) {
        this.lableValue = lableValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}
