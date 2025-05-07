package com.appdev360.jobsitesentry.data.local;

/**
 * Created by abubaker on 1/23/18.
 */

public interface UserDataHelper {

    public void clearAllPref();

    public void storeToken(String token);

    public String getToken();

    public void storeUserId(int id);

    public int getUserId();

    public void storeUserName(String name);

    public void storeUserThumb(String image);

    public String getUserThumb();

    public String getUserName();

    public void storeEmail(String email);

    public String getEmail();

    public void setPosition(int position);

    public int getPosition();

    public void setImagePath(String Path);

    public String getImagePath();

    public void setEncryptedEmail(String email);

    public void setEncryptedPassword(String password);

    public String getEncryptedEmail();

    public String getEncryptedPassword();

    public void enableFingerPrint(boolean enable);

    public boolean isFingerPrintEnable();

    public void saveVector(byte [] vector);

    public String getVector();

    public void setNotificationOn(boolean flag);

    public boolean isNotificationOn();

    public void storeUserRole(String role);

    public String getUserRole();

}
