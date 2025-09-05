package in.eightfolds.winga.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DummyVoucher implements Serializable {

    private String voucherCode;
    private String voucherName;
    private int points;
    private int amount;
    private String imageUrl;
    private int voucherCountSelected;

    public DummyVoucher(){

    }
    public DummyVoucher(String voucherCode, String voucherName, int points, int amount, String imageUrl){
        this.voucherCode = voucherCode;
        this.voucherName = voucherName;
        this.points = points;
        this.amount = amount;
        this.imageUrl = imageUrl;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getVoucherCountSelected() {
        return voucherCountSelected;
    }

    public void setVoucherCountSelected(int voucherCountSelected) {
        this.voucherCountSelected = voucherCountSelected;
    }
}
