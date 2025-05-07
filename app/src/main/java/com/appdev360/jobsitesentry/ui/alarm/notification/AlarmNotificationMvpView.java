package com.appdev360.jobsitesentry.ui.alarm.notification;

import com.appdev360.jobsitesentry.data.model.Data;
import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.ui.base.MvpView;

/**
 * Created by Abubaker on 07,May,2018
 */
public interface AlarmNotificationMvpView extends MvpView {

    void onNotificationReceived(Data notificationData);
    void onError(Error error);
}
