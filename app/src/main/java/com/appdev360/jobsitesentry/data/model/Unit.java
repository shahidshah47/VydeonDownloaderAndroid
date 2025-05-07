package com.appdev360.jobsitesentry.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abubaker on 3/13/18.
 */

public class Unit {

    @SerializedName("site_id")
    private Integer siteId;
    @SerializedName("unit_id")
    private Integer unitId;
    @SerializedName("device_name")
    private String deviceName;
    @SerializedName("ip_address")
    private String ipAddress;
    @SerializedName("port")
    private String port;
    @SerializedName("cameras")
    private ArrayList<Camera> cameras = null;

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public ArrayList<Camera> getCameras() {
        return cameras;
    }

    public void setCameras(ArrayList<Camera> cameras) {
        this.cameras = cameras;
    }

}
