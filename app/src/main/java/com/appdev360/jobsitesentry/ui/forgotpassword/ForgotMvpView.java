package com.appdev360.jobsitesentry.ui.forgotpassword;

import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.ui.base.MvpView;

/**
 * Created by abubaker on 3/12/18.
 */

public interface ForgotMvpView extends MvpView {

    void onSuccess();
    void onError(Error error);
}
