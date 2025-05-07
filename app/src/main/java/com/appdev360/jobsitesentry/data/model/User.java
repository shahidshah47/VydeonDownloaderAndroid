package com.appdev360.jobsitesentry.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by abubaker on 1/23/18.
 */

public class User implements Serializable{

    @SerializedName("user_id")
    private int userId;
    @SerializedName("user_name")
    private String name;
    @SerializedName("user_email")
    private String email;
    @SerializedName("token")
    private String token;



    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
