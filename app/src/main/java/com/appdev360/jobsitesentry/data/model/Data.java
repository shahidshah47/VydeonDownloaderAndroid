package com.appdev360.jobsitesentry.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by abubaker on 1/23/18.
 */

public class Data {

    @SerializedName("user_id")
    private int userId;
    @SerializedName("user_name")
    private String userName;
    @SerializedName("user_email")
    private String userEmail;
    @SerializedName("token")
    private String userToken;
    @SerializedName("company_id")
    private Integer companyId;
    @SerializedName("organization_name")
    private String organizationName;
    @SerializedName("address")
    private String address;
    @SerializedName("phone")
    private String phone;
    @SerializedName("user_role")
    private String userRole;
    @SerializedName("user_thumb")
    private String userThumb;
    @SerializedName("sites")
    private ArrayList<Site> sites = null;
    @SerializedName("arrest_videos")
    private ArrayList<Video> videos = null;
    @SerializedName("cameras")
    private ArrayList<Camera> cameras = null;
    @SerializedName("alarm_notifications")
    private ArrayList<AlarmNotification> alarmNotificationList = null;


    @SerializedName("current_page")
    private int currentPage;
    @SerializedName("next_page")
    private int nextPage;
    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("agreement")
    private String agreement;
    @SerializedName("termOfUse")
    private String termOfUse;
    @SerializedName("privacyPolicy")
    private String privacyPolicy;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArrayList<Site> getSites() {
        return sites;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public void setSites(ArrayList<Site> sites) {
        this.sites = sites;
    }

    public ArrayList<Video> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<Video> videos) {
        this.videos = videos;
    }


    public String getUserThumb() {
        return userThumb;
    }

    public void setUserThumb(String userThumb) {
        this.userThumb = userThumb;
    }

    public ArrayList<Camera> getCameras() {
        return cameras;
    }



    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }


    public ArrayList<AlarmNotification> getAlarmNotificationList() {
        return alarmNotificationList;
    }

    public void setAlarmNotificationList(ArrayList<AlarmNotification> alarmNotificationList) {
        this.alarmNotificationList = alarmNotificationList;
    }

    public String getAgreement() {
        return agreement;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }

    public String getTermOfUse() {
        return termOfUse;
    }

    public void setTermOfUse(String termOfUse) {
        this.termOfUse = termOfUse;
    }

    public String getPrivacyPolicy() {
        return privacyPolicy;
    }

    public void setPrivacyPolicy(String privacyPolicy) {
        this.privacyPolicy = privacyPolicy;
    }
}
