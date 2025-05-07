package com.appdev360.jobsitesentry.ui.sentryunit;

import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.data.model.Video;
import com.appdev360.jobsitesentry.data.model.unit.Data;
import com.appdev360.jobsitesentry.data.model.unit.UnitData;
import com.appdev360.jobsitesentry.ui.base.MvpView;

import java.util.ArrayList;
import java.util.List;

public interface SentryUnitMvpView extends MvpView {

    void showFirstPage(Data unitData);
    void appendNextItems(Data unitData);
    void addLoadingRow();
    void onError(Error error);
}

