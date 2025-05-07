package com.appdev360.jobsitesentry.injection.component;

import android.app.Application;
import android.content.Context;

import com.appdev360.jobsitesentry.data.DataManager;
import com.appdev360.jobsitesentry.data.local.PreferencesHelper;
import com.appdev360.jobsitesentry.data.remote.NetworkService;
import com.appdev360.jobsitesentry.injection.ApplicationContext;
import com.appdev360.jobsitesentry.injection.module.ApplicationModule;
import com.appdev360.jobsitesentry.util.RxEventBus;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Updated By Hussain Saad on 27/01/22
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context context();

    Application application();

    NetworkService newtworkService();

    PreferencesHelper preferencesHelper();

    DataManager dataManager();

    RxEventBus eventBus();
}
