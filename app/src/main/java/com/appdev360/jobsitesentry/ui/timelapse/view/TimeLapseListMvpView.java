package com.appdev360.jobsitesentry.ui.timelapse.view;

import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.data.model.unit.Data;
import com.appdev360.jobsitesentry.ui.base.MvpView;

public interface TimeLapseListMvpView extends MvpView {
    void showData(Data unitData);
    void onError(Error error);
}
