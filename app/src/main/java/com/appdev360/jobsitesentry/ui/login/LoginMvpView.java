package com.appdev360.jobsitesentry.ui.login;

import com.appdev360.jobsitesentry.data.model.Data;
import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.ui.base.MvpView;

/**
 * Created by abubaker on 3/12/18.
 */

public interface LoginMvpView extends MvpView{

    void onLogin(Data data);
    void onError(Error error);

    void onAgreementRequire(String agreement, String terms_of_use, String privay);
    void onAgreementOk();
}
