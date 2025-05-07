package com.appdev360.jobsitesentry.injection.module;

import android.app.Application;
import android.content.Context;


import com.appdev360.jobsitesentry.data.remote.NetworkService;
import com.appdev360.jobsitesentry.injection.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provide application-level dependencies.
 *
 * Updated By Hussain Saad on 27/01/22
 *
 */
@Module
public class ApplicationModule {

    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    NetworkService provideNetworkService() {
        return NetworkService.Creator.networkService(mApplication);
    }



}
