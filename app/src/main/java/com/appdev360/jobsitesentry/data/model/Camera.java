package com.appdev360.jobsitesentry.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by abubaker on 3/13/18.
 */

public class Camera {

    @SerializedName("camera_id")
    @Expose
    private Integer cameraId;
    @SerializedName("camera_name")
    @Expose
    private String cameraName;
    @SerializedName("camera_name_full")
    @Expose
    private String cameraNameFull;
    @SerializedName("camera_link")
    @Expose
    private String cameraLink;
    @SerializedName("local_camera_link")
    @Expose
    private String localCameraLink;


    public Integer getCameraId() {
        return cameraId;
    }

    public void setCameraId(Integer cameraId) {
        this.cameraId = cameraId;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getCameraNameFull() {
        return cameraNameFull;
    }

    public void setCameraNameFull(String cameraNameFull) {
        this.cameraNameFull = cameraNameFull;
    }

    public String getCameraLink() {
        return cameraLink;
    }

    public void setCameraLink(String cameraLink) {
        this.cameraLink = cameraLink;
    }

    public String getLocalCameraLink() {
        return localCameraLink;
    }

    public void setLocalCameraLink(String localCameraLink) {
        this.localCameraLink = localCameraLink;
    }



//    @SerializedName("camera_id")
//    private Integer cameraId;
//    @SerializedName("camera_unit_id")
//    private Integer cameraUnitId;
//    @SerializedName("camera_name")
//    private String cameraName;
//    @SerializedName("camera_name_full")
//    private String cameraNameFull;
//    @SerializedName("camera_link")
//    private String cameraLink;
//    @SerializedName("time_lapse_videos")
//    private ArrayList<TimeLapseMain> timeLapses = null;
//    @SerializedName("alarm_videos")
//    private ArrayList<Alarm> alarmVideo = null;



}
