package in.eightfolds.winga.v2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import in.eightfolds.winga.model.User;
import in.eightfolds.winga.model.UserDeviceDetail;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequest implements Serializable {

    private UserCredentials user;
    private UserDeviceDetail userDeviceDetail;

    public UserCredentials getUser() {
        return user;
    }

    public void setUser(UserCredentials user) {
        this.user = user;
    }

    public UserDeviceDetail getUserDeviceDetail() {
        return userDeviceDetail;
    }

    public void setUserDeviceDetail(UserDeviceDetail userDeviceDetail) {
        this.userDeviceDetail = userDeviceDetail;
    }
}
