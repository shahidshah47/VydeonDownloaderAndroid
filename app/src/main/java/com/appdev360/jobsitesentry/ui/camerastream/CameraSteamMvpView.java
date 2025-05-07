package com.appdev360.jobsitesentry.ui.camerastream;

import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.data.model.Site;
import com.appdev360.jobsitesentry.ui.base.MvpView;

import java.util.ArrayList;

/**
 * Created by abubaker on 3/13/18.
 */

public interface CameraSteamMvpView extends MvpView{

    void onPtzCallCompleted( );
    void onError(Error error);


}
