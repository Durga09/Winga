package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import in.eightfolds.WingaApplication;
import in.eightfolds.winga.R;
import in.eightfolds.winga.utils.Constants;

/**
 * Created by sp on 07/05/18.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Language implements Serializable {

    private Long langId;
    private String isoCode;
    private String title;
    private String dispTitle;
    private boolean isDefault;
    private boolean deleted;
    private String createdTime;
    private String modifiedTime;
    private String locale;
    private boolean selected;
    private String continueTxt;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Long getLangId() {
        return langId;
    }

    public void setLangId(Long langId) {
        this.langId = langId;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDispTitle() {
        return dispTitle;
    }

    public void setDispTitle(String dispTitle) {
        this.dispTitle = dispTitle;
    }

    public boolean isDefault() {
        if(this.isoCode.equalsIgnoreCase(Constants.DEFAULT_ISO)){
            return true;
        }
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getLocale() {
        if(isoCode.equalsIgnoreCase("eng")){
            this.locale = "en";
        }else if(isoCode.equalsIgnoreCase("hin")){
            this.locale = "hi";
        }else if( isoCode.trim().equalsIgnoreCase("tel")){
            this.locale = "te";
        }else if( isoCode.trim().equalsIgnoreCase("ben")){
            this.locale = "bn";
        }else if( isoCode.trim().equalsIgnoreCase("tam")){
            this.locale = "ta";
        }else if( isoCode.trim().equalsIgnoreCase("kan")){
            this.locale = "kn";
        }else if( isoCode.trim().equalsIgnoreCase("mal")){ //malayalam
            this.locale = "ml";
        }else if( isoCode.trim().equalsIgnoreCase("mar")){ //marathi
            this.locale = "mr";
        }else{
            this.locale  = "";
        }
        return this.locale ;
    }


    public String getContinueTxt() {

        if(isoCode.equalsIgnoreCase("eng")){
            this.continueTxt = WingaApplication.getInstance().getApplicationContext().getResources().getString(R.string.to_continue_in_en);
        }else if(isoCode.equalsIgnoreCase("hin")){
            this.continueTxt = WingaApplication.getInstance().getApplicationContext().getResources().getString(R.string.to_continue_in_hi);
        }else if( isoCode.trim().equalsIgnoreCase("tel")){
            this.continueTxt = WingaApplication.getInstance().getApplicationContext().getResources().getString(R.string.to_continue_in_te);
        }else if( isoCode.trim().equalsIgnoreCase("ben")){
            this.continueTxt = WingaApplication.getInstance().getApplicationContext().getResources().getString(R.string.to_continue_in_be);
        }else if( isoCode.trim().equalsIgnoreCase("tam")){
            this.continueTxt = WingaApplication.getInstance().getApplicationContext().getResources().getString(R.string.to_continue_in_ta);
        }else if( isoCode.trim().equalsIgnoreCase("kan")){
            this.continueTxt = WingaApplication.getInstance().getApplicationContext().getResources().getString(R.string.to_continue_in_kn);
        }else if( isoCode.trim().equalsIgnoreCase("mal")){ //malayalam
            this.continueTxt = WingaApplication.getInstance().getApplicationContext().getResources().getString(R.string.to_continue_in_ml);
        }else if( isoCode.trim().equalsIgnoreCase("mar")){ //marathi
            this.continueTxt = WingaApplication.getInstance().getApplicationContext().getResources().getString(R.string.to_continue_in_mr);
        }else{
            this.continueTxt  = WingaApplication.getInstance().getApplicationContext().getResources().getString(R.string.to_continue_in_en);
        }

        return continueTxt;
    }

    public void setContinueTxt(String continueTxt) {
        this.continueTxt = continueTxt;
    }

    public void setLocale(String locale) {

        this.locale = locale;
    }

    @Override
    public String toString() {
        return "Language [langId=" + langId + ", isoCode=" + isoCode
                + ", title=" + title + ", dispTitle=" + dispTitle
                + ", isDefault=" + isDefault + ", deleted=" + deleted
                + ", createdTime=" + createdTime + "] ";
    }
}