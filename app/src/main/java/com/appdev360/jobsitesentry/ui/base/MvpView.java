package com.appdev360.jobsitesentry.ui.base;


import com.appdev360.jobsitesentry.data.model.Error;

/**
 * Base interface that any class that wants to act as a View in the MVP (Model View Presenter)
 * pattern must implement. Generally this interface will be extended by a more specific interface
 * that then usually will be implemented by an Activity or Fragment.
 */
public interface MvpView {


    void onTokenExpire(Error error  );
    void onChangePasswrod(Error error);

}
