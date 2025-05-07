
package com.appdev360.jobsitesentry.data.model.timelapse;

import java.util.ArrayList;
import java.util.List;

import com.appdev360.jobsitesentry.data.model.unit.Links;
import com.appdev360.jobsitesentry.data.model.unit.Meta;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("data")
    @Expose
    private ArrayList<TimeLapse> timeLapseData = null;
    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("links")
    @Expose
    private Links links;

    public ArrayList<TimeLapse> getTimeLapseData() {
        return timeLapseData;
    }

    public void setTimeLapseData(ArrayList<TimeLapse> timeLapseData) {
        this.timeLapseData = timeLapseData;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

}
