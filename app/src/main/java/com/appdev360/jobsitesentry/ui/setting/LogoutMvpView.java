package com.appdev360.jobsitesentry.ui.setting;

import com.appdev360.jobsitesentry.ui.base.MvpView;
import com.appdev360.jobsitesentry.data.model.Error;

/**
 * Created by Abubaker on 04,May,2018
 */
public interface LogoutMvpView extends MvpView {

    void onLogout(String message);
    void onLogoutError(Error error);
}
