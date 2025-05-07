
package com.appdev360.jobsitesentry.data.model.timelapse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeLapse {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("filename")
    @Expose
    private String filename;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

}
