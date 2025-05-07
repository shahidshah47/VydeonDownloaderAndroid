package com.appdev360.jobsitesentry.ui.setting;

import com.appdev360.jobsitesentry.data.model.Data;
import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.ui.base.MvpView;

/**
 * Created by Abubaker on 25,April,2018
 */
public interface SettingMvpView extends MvpView {

    void onUpdateProfile(Data data);
    void onProfileError(Error error);
}
