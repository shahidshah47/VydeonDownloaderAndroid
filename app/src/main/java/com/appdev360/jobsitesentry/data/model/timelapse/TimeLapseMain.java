
package com.appdev360.jobsitesentry.data.model.timelapse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeLapseMain {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("status_code")
    @Expose
    private Integer statusCode;
    @SerializedName("eventType")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;
    
    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
