package com.appdev360.jobsitesentry.ui.timelapse.view;

import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.data.model.timelapse.Data;
import com.appdev360.jobsitesentry.ui.base.MvpView;

public interface TimeLapseMvpView extends MvpView {
    void showFirstPage(Data unitData);
    void appendNextItems(Data unitData);
    void addLoadingRow();
    void onError(Error error);
}
