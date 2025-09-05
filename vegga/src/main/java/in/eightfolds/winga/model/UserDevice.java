package in.eightfolds.winga.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class UserDevice implements Serializable {
    private String createdTime;

    private String userId;

    private String modifiedTime;

    private String imei;

    private String pushRegId;

    private int platformId;

    private String deviceId;

    public String getGaid() {
        return gaid;
    }

    public void setGaid(String gaid) {
        this.gaid = gaid;
    }

    private String gaid;

    public String getCreatedTime ()
    {
        return createdTime;
    }

    public void setCreatedTime (String createdTime)
    {
        this.createdTime = createdTime;
    }

    public String getUserId ()
    {
        return userId;
    }

    public void setUserId (String userId)
    {
        this.userId = userId;
    }

    public String getModifiedTime ()
    {
        return modifiedTime;
    }

    public void setModifiedTime (String modifiedTime)
    {
        this.modifiedTime = modifiedTime;
    }

    public String getImei ()
    {
        return imei;
    }

    public void setImei (String imei)
    {
        this.imei = imei;
    }

    public String getPushRegId ()
    {
        return pushRegId;
    }

    public void setPushRegId (String pushRegId)
    {
        this.pushRegId = pushRegId;
    }

    public int getPlatformId ()
    {
        return platformId;
    }

    public void setPlatformId (int platformId)
    {
        this.platformId = platformId;
    }

    public String getDeviceId ()
    {
        return deviceId;
    }

    public void setDeviceId (String deviceId)
    {
        this.deviceId = deviceId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [createdTime = "+createdTime+", userId = "+userId+", modifiedTime = "+modifiedTime+", imei = "+imei+", pushRegId = "+pushRegId+", platformId = "+platformId+", deviceId = "+deviceId+"]";
    }
}
