package com.appdev360.jobsitesentry.ui.notificationsetting;

import com.appdev360.jobsitesentry.ui.base.MvpView;
import com.appdev360.jobsitesentry.data.model.Error;

/**
 * Created by Abubaker on 03,May,2018
 */
public interface NotificationMvpView extends MvpView{

   void onSuccess(String message);
   void onNotificationError(Error error);
}
