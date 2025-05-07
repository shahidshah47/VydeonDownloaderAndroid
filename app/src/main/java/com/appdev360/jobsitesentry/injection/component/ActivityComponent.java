package com.appdev360.jobsitesentry.injection.component;


import com.appdev360.jobsitesentry.injection.PerActivity;
import com.appdev360.jobsitesentry.injection.module.ActivityModule;
import com.appdev360.jobsitesentry.ui.agreement.AgreementActivity;
import com.appdev360.jobsitesentry.ui.alarm.AlarmFragment;
import com.appdev360.jobsitesentry.ui.alarm.notification.AlarmNotificationFragment;
import com.appdev360.jobsitesentry.ui.arrest.ArrestFragment;
import com.appdev360.jobsitesentry.ui.base.BaseFragment;
import com.appdev360.jobsitesentry.ui.camerastream.CameraStreamFragment;
import com.appdev360.jobsitesentry.ui.changepassword.ChangePassword;
import com.appdev360.jobsitesentry.ui.dashboard.DashBoardFragment;
import com.appdev360.jobsitesentry.ui.drone.DroneFragment;
import com.appdev360.jobsitesentry.ui.drone.DroneListingFragment;
import com.appdev360.jobsitesentry.ui.forgotpassword.ForgotPasswordFragment;
import com.appdev360.jobsitesentry.ui.login.LoginFragment;
import com.appdev360.jobsitesentry.ui.notificationsetting.NotificationSetting;
import com.appdev360.jobsitesentry.ui.sentryLocation.SentryLocationActivity;
import com.appdev360.jobsitesentry.ui.sentryLocation.SentryLocationFragment;
import com.appdev360.jobsitesentry.ui.sentryunit.SentryUnitFragment;
import com.appdev360.jobsitesentry.ui.setting.SettingFragment;
import com.appdev360.jobsitesentry.ui.splash.SplashActivity;
import com.appdev360.jobsitesentry.ui.timelapse.fragment.TimeLapseFragment;
import com.appdev360.jobsitesentry.ui.timelapse.fragment.TimeLapseListFragment;

import dagger.Subcomponent;

/**
 * Created by abubaker on 3/12/18.
 * <p>
 * Updated By Hussain Saad on 27/01/22
 */

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

  void inject(SplashActivity splash);
  void inject(SentryLocationActivity sentryLocationActivity);

  void inject(BaseFragment baseFragment);
  void inject(LoginFragment loginFragment);
  void inject(ForgotPasswordFragment forgotPassword);
  void inject(SentryLocationFragment sentryLocationFragment);
  void inject(SentryUnitFragment sentryUnitFragment);
  void inject(CameraStreamFragment cameraStreamFragment);
  void inject(SettingFragment settingFragment);
  void inject(ChangePassword changePassword);
  void inject(DashBoardFragment dashBoardFragment);
  void inject(TimeLapseFragment timeLapseFragment);
  void inject(DroneFragment droneFragment);
  void inject(DroneListingFragment droneListingFragment);
  void inject(ArrestFragment arrestFragment);
  void inject(AlarmFragment arrestFragment);
  void inject(NotificationSetting notificationSetting);
  void inject(AlarmNotificationFragment alarmNotificationFragment);
  void inject(TimeLapseListFragment timeLapListFragment);
  void inject(AgreementActivity agreementActivity);


}
