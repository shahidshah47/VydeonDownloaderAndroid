package com.appdev360.jobsitesentry.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jaffarraza on 16/05/2017.
 */

public class BaseModel {

    @SerializedName("status")
    private boolean status;
    @SerializedName("status_code")
    private int statusCode;
    @SerializedName("eventType")
    private String message;
    @SerializedName("data")
    private Data data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}