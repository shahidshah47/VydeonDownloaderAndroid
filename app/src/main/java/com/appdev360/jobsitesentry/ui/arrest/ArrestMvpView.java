package com.appdev360.jobsitesentry.ui.arrest;

import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.data.model.Video;
import com.appdev360.jobsitesentry.ui.base.MvpView;

import java.util.ArrayList;

/**
 * Created by Abubaker on 10,April,2018
 */
public interface ArrestMvpView extends MvpView {

    void showItems(ArrayList<Video> items);
    void onError(Error error);
}
