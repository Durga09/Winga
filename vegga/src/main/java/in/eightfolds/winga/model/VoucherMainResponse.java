package in.eightfolds.winga.model;
import java.io.Serializable;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class VoucherMainResponse implements Serializable{

    private static final long serialVersionUID = 1L;
    private String success;
    private List<Voucher> vouchers;
    private String count;
    private String order_id;

    @JsonProperty("success")
    public String getSuccess() {
        return success;
    }
    @JsonProperty("vouchers")
    public List<Voucher> getVouchers() {
        return vouchers;
    }
    @JsonProperty("count")
    public String getCount() {
        return count;
    }
    @JsonProperty("order_id")
    public String getOrder_id() {
        return order_id;
    }

}
