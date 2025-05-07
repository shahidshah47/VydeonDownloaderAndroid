package com.appdev360.jobsitesentry.ui.sentryLocation;

import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.data.model.Site;
import com.appdev360.jobsitesentry.ui.base.MvpView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abubaker on 3/13/18.
 */

public interface SentryLocationMvpView extends MvpView{

    void showItems(ArrayList<Site> items);
    void onError(Error error);
}
