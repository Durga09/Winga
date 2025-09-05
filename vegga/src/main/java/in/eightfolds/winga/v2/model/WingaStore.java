package in.eightfolds.winga.v2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WingaStore implements Serializable {

    public ArrayList<Banner> bannerItems;
    public ArrayList<Category> categories;
    public ArrayList<TopBrands> topBrands;


    public ArrayList<Banner> getBannerItems() {
        return bannerItems;
    }

    public void setBannerItems(ArrayList<Banner> bannerItems) {
        this.bannerItems = bannerItems;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public ArrayList<TopBrands> getTopBrands() {
        return topBrands;
    }

    public void setTopBrands(ArrayList<TopBrands> topBrands) {
        this.topBrands = topBrands;
    }
}
