package com.appdev360.jobsitesentry.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

/**
 * Created by ahsan on 4/18/17.
 */
@Singleton
public class PreferencesDataHelper {

    private static final String GENERAL_PREFERENCE_NAME = "generalpreferences";

    public enum PersistenceKey {TOKEN, LOGGEDIN_USER,USER_ID,USER_NAME,EMAIL,CAMERA_POSITION,IMAGE_PATH,USER_EMAIL,
        USER_PASSWORD,FINGER_PRINT,VECTOR,USER_THUMB,CHECKED_NOTIFICATION,USER_ROLE}


    public static void store(Context context, PersistenceKey key, String value) {

        SharedPreferences settings = context.getSharedPreferences(GENERAL_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key.toString(), value);
        editor.apply();
    }

    public static void store(Context context, PersistenceKey key, int value) {

        SharedPreferences settings = context.getSharedPreferences(GENERAL_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key.toString(), value);
        editor.apply();
    }

    public static void store(Context context, PersistenceKey key, boolean value) {

        SharedPreferences settings = context.getSharedPreferences(GENERAL_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key.toString(), value);
        editor.apply();
    }

    public static String retrieve(Context context, PersistenceKey key) {

        SharedPreferences settings = context.getSharedPreferences(GENERAL_PREFERENCE_NAME, 0);
        return settings.getString(key.toString(), null);
    }

    public static int retrieveInt(Context context, PersistenceKey key) {

        SharedPreferences settings = context.getSharedPreferences(GENERAL_PREFERENCE_NAME, 0);
        return settings.getInt(key.toString(), -1);
    }

    public static boolean retrieveBoolean(Context context, PersistenceKey key) {

        SharedPreferences settings = context.getSharedPreferences(GENERAL_PREFERENCE_NAME, 0);
        return settings.getBoolean(key.toString(), false);
    }



    public static void clearPref(Context context){
        SharedPreferences settings = context.getSharedPreferences(GENERAL_PREFERENCE_NAME, 0);
        settings.edit().clear().apply();
    }


}
