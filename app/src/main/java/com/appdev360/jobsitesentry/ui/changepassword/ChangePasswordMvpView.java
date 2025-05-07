package com.appdev360.jobsitesentry.ui.changepassword;

import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.ui.base.MvpView;

/**
 * Created by abubaker on 3/21/18.
 */

public interface ChangePasswordMvpView extends MvpView{

    void onPasswordChange(String message);
    void onError(Error error);
}
