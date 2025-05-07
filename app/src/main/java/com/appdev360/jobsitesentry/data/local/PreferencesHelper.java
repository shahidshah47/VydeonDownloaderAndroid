package com.appdev360.jobsitesentry.data.local;

import android.content.Context;
import android.util.Base64;

import com.appdev360.jobsitesentry.injection.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class PreferencesHelper implements UserDataHelper {

    private Context context;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        this.context=context;
    }

    @Override
    public void clearAllPref() {
        PreferencesDataHelper.clearPref(context);
    }

    @Override
    public void storeToken(String token) {
        PreferencesDataHelper.store(context, PreferencesDataHelper.PersistenceKey.TOKEN, token);
    }

    @Override
    public String getToken() {
        return PreferencesDataHelper.retrieve(context, PreferencesDataHelper.PersistenceKey.TOKEN);
    }

    @Override
    public void storeUserId(int id) {
        PreferencesDataHelper.store(context, PreferencesDataHelper.PersistenceKey.USER_ID, id);

    }

    @Override
    public int getUserId() {
        return PreferencesDataHelper.retrieveInt(context, PreferencesDataHelper.PersistenceKey.USER_ID);
    }

    @Override
    public void storeUserName(String name) {
        PreferencesDataHelper.store(context, PreferencesDataHelper.PersistenceKey.USER_NAME, name);
    }

    @Override
    public void storeUserThumb(String image) {
        PreferencesDataHelper.store(context, PreferencesDataHelper.PersistenceKey.USER_THUMB, image);
    }

    @Override
    public String getUserThumb() {
        return PreferencesDataHelper.retrieve(context, PreferencesDataHelper.PersistenceKey.USER_THUMB);
    }

    @Override
    public String getUserName() {
        return PreferencesDataHelper.retrieve(context, PreferencesDataHelper.PersistenceKey.USER_NAME);
    }

    @Override
    public void storeEmail(String email) {
        PreferencesDataHelper.store(context, PreferencesDataHelper.PersistenceKey.EMAIL, email);
    }

    @Override
    public String getEmail() {
        return PreferencesDataHelper.retrieve(context, PreferencesDataHelper.PersistenceKey.EMAIL);
    }

    @Override
    public void setPosition(int position) {
        PreferencesDataHelper.store(context, PreferencesDataHelper.PersistenceKey.CAMERA_POSITION, position);
    }

    @Override
    public int getPosition() {
        return PreferencesDataHelper.retrieveInt(context, PreferencesDataHelper.PersistenceKey.CAMERA_POSITION);
    }

    @Override
    public void setImagePath(String path) {
        PreferencesDataHelper.store(context, PreferencesDataHelper.PersistenceKey.IMAGE_PATH, path);
    }

    @Override
    public String getImagePath() {
        return PreferencesDataHelper.retrieve(context, PreferencesDataHelper.PersistenceKey.IMAGE_PATH);
    }

    @Override
    public void setEncryptedEmail(String username) {
        PreferencesDataHelper.store(context, PreferencesDataHelper.PersistenceKey.USER_EMAIL, username);
    }

    @Override
    public void setEncryptedPassword(String password) {
        PreferencesDataHelper.store(context, PreferencesDataHelper.PersistenceKey.USER_PASSWORD, password);
    }

    @Override
    public String getEncryptedEmail() {
        return PreferencesDataHelper.retrieve(context, PreferencesDataHelper.PersistenceKey.USER_EMAIL);
    }

    @Override
    public String getEncryptedPassword() {
        return PreferencesDataHelper.retrieve(context, PreferencesDataHelper.PersistenceKey.USER_PASSWORD);
    }

    @Override
    public void enableFingerPrint(boolean enable) {
        PreferencesDataHelper.store(context, PreferencesDataHelper.PersistenceKey.FINGER_PRINT, enable);
    }

    @Override
    public boolean isFingerPrintEnable() {
        return PreferencesDataHelper.retrieveBoolean(context, PreferencesDataHelper.PersistenceKey.FINGER_PRINT);
    }

    @Override
    public void saveVector(byte[] vector) {
        PreferencesDataHelper.store(context, PreferencesDataHelper.PersistenceKey.VECTOR, Base64.encodeToString(vector,
                Base64.DEFAULT));
    }

    @Override
    public String getVector() {
        return PreferencesDataHelper.retrieve(context, PreferencesDataHelper.PersistenceKey.VECTOR);
    }

    @Override
    public void setNotificationOn(boolean flag) {
        PreferencesDataHelper.store(context, PreferencesDataHelper.PersistenceKey.CHECKED_NOTIFICATION, flag);
    }

    @Override
    public boolean isNotificationOn() {
        return PreferencesDataHelper.retrieveBoolean(context, PreferencesDataHelper.PersistenceKey.CHECKED_NOTIFICATION);
    }

    @Override
    public void storeUserRole(String role) {
        PreferencesDataHelper.store(context, PreferencesDataHelper.PersistenceKey.USER_ROLE, role);
    }

    @Override
    public String getUserRole() {
        return PreferencesDataHelper.retrieve(context, PreferencesDataHelper.PersistenceKey.USER_ROLE);
    }


}
