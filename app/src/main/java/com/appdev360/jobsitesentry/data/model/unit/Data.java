
package com.appdev360.jobsitesentry.data.model.unit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Data {


    @SerializedName("data")
    @Expose
    private ArrayList<UnitData> unitData = null;
    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("links")
    @Expose
    private Links links;

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

    public ArrayList<UnitData> getUnitData() {
        return unitData;
    }

    public void setUnitData(ArrayList<UnitData> unitData) {
        this.unitData = unitData;
    }


}
