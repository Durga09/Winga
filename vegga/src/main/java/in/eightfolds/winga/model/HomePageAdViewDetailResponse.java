package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
import in.eightfolds.winga.model.HomePageAdViewDetail;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)


public class HomePageAdViewDetailResponse implements Serializable {


	private List<HomePageAdViewDetail> advertisementViewDetails;


	public List<HomePageAdViewDetail> getAdvertisementViewDetails() {
		return advertisementViewDetails;
	}

	public void setAdvertisementViewDetails(
			List<HomePageAdViewDetail> advertisementViewDetails) {
		this.advertisementViewDetails = advertisementViewDetails;
	}


}
