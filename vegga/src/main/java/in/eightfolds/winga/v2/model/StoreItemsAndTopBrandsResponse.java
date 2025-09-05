package in.eightfolds.winga.v2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreItemsAndTopBrandsResponse implements Serializable {

    public ArrayList<StoreItem> storeItems;
    public ArrayList<Banner> bannerItems;
    public ArrayList<TopBrands> topBrands;
    public String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ArrayList<StoreItem> getStoreItems() {
        return storeItems;
    }

    public void setStoreItems(ArrayList<StoreItem> storeItems) {
        this.storeItems = storeItems;
    }

    public ArrayList<Banner> getBannerItems() {
        return bannerItems;
    }

    public void setBannerItems(ArrayList<Banner> bannerItems) {
        this.bannerItems = bannerItems;
    }

    public ArrayList<TopBrands> getTopBrands() {
        return topBrands;
    }

    public void setTopBrands(ArrayList<TopBrands> topBrands) {
        this.topBrands = topBrands;
    }
}
