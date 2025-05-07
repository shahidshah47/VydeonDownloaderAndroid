
package com.appdev360.jobsitesentry.data.model.unit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links {

    @SerializedName("prev")
    @Expose
    private Object prev;
    @SerializedName("next")
    @Expose
    private String next;

    public Object getPrev() {
        return prev;
    }

    public void setPrev(Object prev) {
        this.prev = prev;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

}
