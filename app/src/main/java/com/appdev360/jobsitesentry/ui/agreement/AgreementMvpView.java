package com.appdev360.jobsitesentry.ui.agreement;

import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.ui.base.MvpView;

public interface AgreementMvpView extends MvpView {
    void onAgreementSuccess();
    void onAgreementFailure(Error error);
    void onLogoutError(Error error);
    void onLogout(String message);
}
