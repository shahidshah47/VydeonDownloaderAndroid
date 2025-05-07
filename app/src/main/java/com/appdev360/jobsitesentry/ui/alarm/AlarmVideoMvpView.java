package com.appdev360.jobsitesentry.ui.alarm;

import com.appdev360.jobsitesentry.data.model.Camera;
import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.ui.base.MvpView;

import java.util.ArrayList;

/**
 * Created by Abubaker on 27,April,2018
 */
public interface AlarmVideoMvpView extends MvpView {

    void showVideos(ArrayList<Camera> cameras);
    void onError(Error error);

}
