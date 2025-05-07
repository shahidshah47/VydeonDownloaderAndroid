package com.appdev360.jobsitesentry.constant;

/**
 * Created by abubaker on 3/12/18.
 * <p>
 * Updated By Hussain Saad on 27/01/22
 */

public class NetworkConstants {

    public  static class Params {
        public static final String AUTHORIZATION_PARAM = "Authorization";
        public static final String EMAIL_PARAM = "email";
        public static final String UNIT_ID_PARAM = "unit_id";
        public static final String PASSWORD_PARAM = "password";
        public static final String FCM_TOKEN = "fcm_token";
        public static final String TYPE = "type";
        public static final String BUILD_VERSION = "build_version";
        public static final String ENABLED = "enabled";
        public static final String START_TIME = "start_time";
        public static final String PAGE_NO = "page_no";
        public static final String END_TIME = "end_time";
        public static final String NEW_PASSWORD_PARAM = "new_password";
        public static final String IMAGE = "image";
        public static final String TIME_ZONE = "timezone";
        public static final String TIME = "licence_ac_date";

    }

    public static final int TIME_OUT = 30;
//    public static final String BASE_URL = "http://jobsitesentry.vteamslabs.com/api/"; // Staging
    public static final String BASE_URL = "https://myjobsitesentry.com/api/"; // Production
    public static final String LOGIN_PATH = "user/login";
    public static final String LOGOUT_PATH = "user/logout";
    public static final String FORGOT_PASSWORD_PATH = "user/password/forgot";
    public static final String CHANGE_PASSWORD_PATH = "user/password/change";
    public static final String USER_UPDATE = "user/update";
    public static final String LOCATION_LIST_PATH = "site/v2/complete-sites-list";
    public static final String DRONE_LIST_PATH = "site/complete-drones-list";
    public static final String ARREST_LIST_PATH = "site/arrest-videos-list";
    public static final String ALARM_VIDEO_LIST = "site/alarm-videos-list";
    public static final String LOCATION_UNITS = "site/units";
    public static final String TIME_LAPSE_VIDEOS = "site/units/timelapsvideos";
    public static final String TOKEN_VALIDITY = "user/check_token";
    public static final String AGREEMENT_SIGN = "user/signAgreement";


    public static final String UPDATE_SETTING = "user/update_settings";
    public static final String SITE_ALARM_NOTIFICATION = "site/alarm_notifications";
    public static final String UPDATE_TOKEN = "user/update_devicetoken";
    public static final String USER_LOGOUT = "user/logout";

    public static final String ACTION_LOGOUT = "agreement expired";
    public static final String AGREEMENT_URL = BASE_URL + "page/agreement";

}
