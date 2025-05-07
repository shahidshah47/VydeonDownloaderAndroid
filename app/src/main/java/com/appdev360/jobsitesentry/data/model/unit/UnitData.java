package com.appdev360.jobsitesentry.data.model.unit;

import com.appdev360.jobsitesentry.data.model.Camera;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class UnitData {
    
    @SerializedName("ptz_enabled")
    @Expose
    private Integer ptzEnabled;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("site_id")
    @Expose
    private Integer siteId;
    @SerializedName("device_name")
    @Expose
    private String deviceName;
    @SerializedName("ip_address")
    @Expose
    private String ipAddress;
    @SerializedName("port")
    @Expose
    private String port;
    @SerializedName("alarm_uri")
    @Expose
    private String alarmUri;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("username")
    @Expose
    private Object username;
    @SerializedName("password")
    @Expose
    private Object password;
    @SerializedName("u_id")
    @Expose
    private String uId;
    @SerializedName("cameras")
    @Expose
    private ArrayList<Camera> cameras = null;
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getSiteId() {
        return siteId;
    }
    
    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public boolean getPtzEnabled() {
        return ptzEnabled!=0;
    }

    public void setPtzEnabled(Integer ptzEnabled) {
        this.ptzEnabled = ptzEnabled;
    }

    public String getDeviceName() {
        return deviceName;
    }
    
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public String getPort() {
        return port;
    }
    
    public void setPort(String port) {
        this.port = port;
    }
    
    public String getAlarmUri() {
        return alarmUri;
    }
    
    public void setAlarmUri(String alarmUri) {
        this.alarmUri = alarmUri;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Object getUsername() {
        return username;
    }
    
    public void setUsername(Object username) {
        this.username = username;
    }
    
    public Object getPassword() {
        return password;
    }
    
    public void setPassword(Object password) {
        this.password = password;
    }
    
    public String getUId() {
        return uId;
    }
    
    public void setUId(String uId) {
        this.uId = uId;
    }
    
    public ArrayList<Camera> getCameras() {
        int i = 0;
        
         if(type.equalsIgnoreCase("dahua")){
            for (Camera camera : cameras) {
                {

                    String rtspUrl = "rtsp://" + username + ":" + password + "@" + ipAddress + ":" + port + "/cam/realmonitor?channel="+  ++i +"&subtype=1";
                    camera.setCameraLink(rtspUrl);
                }

            }
        } else if (!(type.equalsIgnoreCase("rialto")||type.equalsIgnoreCase("xavier"))) {
            for (Camera camera : cameras) {
                {
                    String rtspUrl = "rtsp://" + username + ":" + password + "@" + ipAddress + ":" + port + "/live/" + ++i + "";
                    camera.setCameraLink(rtspUrl);
                }

            }
        }
        return cameras;
    }
    
    public void setCameras(ArrayList<Camera> cameras) {
        this.cameras = cameras;
    }
    public static String getMd5(String input)
    {
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    
}
