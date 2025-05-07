package com.appdev360.jobsitesentry.data;

import android.content.Context;

import com.appdev360.jobsitesentry.data.local.PreferencesHelper;
import com.appdev360.jobsitesentry.data.remote.NetworkService;
import com.appdev360.jobsitesentry.injection.ApplicationContext;

import javax.inject.Inject;

/**
 * Created by abubaker on 3/12/18.
 * <p>
 * Updated By Hussain Saad on 27/01/22
 */

public class DataManager {

    private final NetworkService mNetWorkService;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public DataManager(NetworkService networkService, PreferencesHelper preferencesHelper, @ApplicationContext Context context) {
        mNetWorkService = networkService;
        mPreferencesHelper = preferencesHelper;
        // this.mDatabaseRealm.setmContext(context);

    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public NetworkService getNetworkService() {
        return mNetWorkService;
    }

}
