package in.eightfolds.winga.v2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InstareelResponse implements Serializable {

    public String termsAndConditions;
    public ArrayList<InstaReelItem> instaReelItems;
    public ArrayList<InstaReelType> instaReelTypes;

    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(String termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    public ArrayList<InstaReelItem> getInstaReelItems() {
        return instaReelItems;
    }

    public void setInstaReelItems(ArrayList<InstaReelItem> instaReelItems) {
        this.instaReelItems = instaReelItems;
    }

    public ArrayList<InstaReelType> getInstaReelTypes() {
        return instaReelTypes;
    }

    public void setInstaReelTypes(ArrayList<InstaReelType> instaReelTypes) {
        this.instaReelTypes = instaReelTypes;
    }
}
