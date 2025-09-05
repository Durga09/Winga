package in.eightfolds.winga.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class Voucher implements Serializable {

    private static final long serialVersionUID = 1L;
    private String product_id;
    private String product_name;
    private String product_description;
    private String product_terms_conditions;
    private String product_expiry_and_validity;
    private String product_how_to_use;
    private String product_image;
    private String currency_code;
    private String currency_name;
    private String country_name;
    private List<String> product_denminations;
    private String code;
    private String pin;
    private String validity_date;
    private String amount;
    private double denomination;
    private Long imageId;


    private int loyalityPoints;
    private int voucherCountSelected;


    @JsonProperty("product_id")
    public String getProduct_id() {
        return product_id;
    }
    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
    @JsonProperty("product_name")
    public String getProduct_name() {
        return product_name;
    }
    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
    @JsonProperty("product_description")
    public String getProduct_description() {
        return product_description;
    }
    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }
    @JsonProperty("product_terms_conditions")
    public String getProduct_terms_conditions() {
        return product_terms_conditions;
    }
    public void setProduct_terms_conditions(String product_terms_conditions) {
        this.product_terms_conditions = product_terms_conditions;
    }
    @JsonProperty("product_expiry_and_validity")
    public String getProduct_expiry_and_validity() {
        return product_expiry_and_validity;
    }
    public void setProduct_expiry_and_validity(String product_expiry_and_validity) {
        this.product_expiry_and_validity = product_expiry_and_validity;
    }
    @JsonProperty("product_how_to_use")
    public String getProduct_how_to_use() {
        return product_how_to_use;
    }
    public void setProduct_how_to_use(String product_how_to_use) {
        this.product_how_to_use = product_how_to_use;
    }
    @JsonProperty("product_image")
    public String getProduct_image() {
        return product_image;
    }
    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }
    @JsonProperty("currency_code")
    public String getCurrency_code() {
        return currency_code;
    }
    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }
    @JsonProperty("success")
    public String getCurrency_name() {
        return currency_name;
    }
    public void setCurrency_name(String currency_name) {
        this.currency_name = currency_name;
    }
    @JsonProperty("country_name")
    public String getCountry_name() {
        return country_name;
    }
    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }
    @JsonProperty("product_denminations")
    public List<String> getProduct_denminations() {
        return product_denminations;
    }
    @JsonProperty("code")
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    @JsonProperty("pin")
    public String getPin() {
        return pin;
    }
    public void setPin(String pin) {
        this.pin = pin;
    }
    @JsonProperty("validity_date")
    public String getValidity_date() {
        return validity_date;
    }
    public void setValidity_date(String validity_date) {
        this.validity_date = validity_date;
    }
    @JsonProperty("amount")
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public void setProduct_denminations(List<String> product_denminations) {
        this.product_denminations = product_denminations;
    }

    public int getVoucherCountSelected() {
        return voucherCountSelected;
    }

    public void setVoucherCountSelected(int voucherCountSelected) {
        this.voucherCountSelected = voucherCountSelected;
    }

    public double getDenomination() {
        return denomination;
    }

    public void setDenomination(double denomination) {
        this.denomination = denomination;
    }


    public int getLoyalityPoints() {
        return loyalityPoints;
    }

    public void setLoyalityPoints(int loyalityPoints) {
        this.loyalityPoints = loyalityPoints;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }
}
