package com.appdev360.jobsitesentry.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by abubaker on 3/13/18.
 */

public class Site {

    @SerializedName("company_id")
    private Integer companyId;
    @SerializedName("site_id")
    private Integer siteId;
    @SerializedName("site_name")
    private String siteName;
    @SerializedName("site_address")
    private String siteAddress;
    @SerializedName("site_phone_number")
    private String sitePhoneNumber;
    @SerializedName("units")
    private ArrayList<Unit> units = null;
    @SerializedName("drone_videos")
    private ArrayList<Video> droneVideos = null;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteAddress() {
        return siteAddress;
    }

    public void setSiteAddress(String siteAddress) {
        this.siteAddress = siteAddress;
    }

    public String getSitePhoneNumber() {
        return sitePhoneNumber;
    }

    public void setSitePhoneNumber(String sitePhoneNumber) {
        this.sitePhoneNumber = sitePhoneNumber;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<Unit> units) {
        this.units = units;
    }

    public ArrayList<Video> getDroneVideos() {
        return droneVideos;
    }

    public void setDroneVideos(ArrayList<Video> droneVideos) {
        this.droneVideos = droneVideos;
    }

}
