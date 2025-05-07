package com.appdev360.jobsitesentry.data.remote;


import android.content.Context;

import com.appdev360.jobsitesentry.constant.GeneralConstants;
import com.appdev360.jobsitesentry.constant.NetworkConstants;
import com.appdev360.jobsitesentry.data.model.AgreementModel;
import com.appdev360.jobsitesentry.data.model.BaseModel;
import com.appdev360.jobsitesentry.data.model.timelapse.TimeLapseMain;
import com.appdev360.jobsitesentry.data.model.token_validity.TokenValidityGson;
import com.appdev360.jobsitesentry.data.model.unit.UnitDataMain;
import com.appdev360.jobsitesentry.injection.ApplicationContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

import static com.appdev360.jobsitesentry.constant.NetworkConstants.AGREEMENT_SIGN;
import static com.appdev360.jobsitesentry.constant.NetworkConstants.ALARM_VIDEO_LIST;
import static com.appdev360.jobsitesentry.constant.NetworkConstants.ARREST_LIST_PATH;
import static com.appdev360.jobsitesentry.constant.NetworkConstants.CHANGE_PASSWORD_PATH;
import static com.appdev360.jobsitesentry.constant.NetworkConstants.DRONE_LIST_PATH;
import static com.appdev360.jobsitesentry.constant.NetworkConstants.FORGOT_PASSWORD_PATH;
import static com.appdev360.jobsitesentry.constant.NetworkConstants.LOCATION_LIST_PATH;
import static com.appdev360.jobsitesentry.constant.NetworkConstants.LOCATION_UNITS;
import static com.appdev360.jobsitesentry.constant.NetworkConstants.LOGIN_PATH;
import static com.appdev360.jobsitesentry.constant.NetworkConstants.Params.AUTHORIZATION_PARAM;
import static com.appdev360.jobsitesentry.constant.NetworkConstants.Params.BUILD_VERSION;
import static com.appdev360.jobsitesentry.constant.NetworkConstants.Params.EMAIL_PARAM;
import static com.appdev360.jobsitesentry.constant.NetworkConstants.Params.ENABLED;
import static com.appdev360.jobsitesentry.constant.NetworkConstants.Params.END_TIME;
import static com.appdev360.jobsitesentry.constant.NetworkConstants.Params.FCM_TOKEN;
import static com.appdev360.jobsitesentry.constant.NetworkConstants.Params.NEW_PASSWORD_PARAM;
import static com.appdev360.jobsitesentry.constant.NetworkConstants.Params.PAGE_NO;
import static com.appdev360.jobsitesentry.constant.NetworkConstants.Params.PASSWORD_PARAM;
import static com.appdev360.jobsitesentry.constant.NetworkConstants.Params.START_TIME;
import static com.appdev360.jobsitesentry.constant.NetworkConstants.Params.TIME_ZONE;
import static com.appdev360.jobsitesentry.constant.NetworkConstants.Params.TYPE;
import static com.appdev360.jobsitesentry.constant.NetworkConstants.Params.UNIT_ID_PARAM;
import static com.appdev360.jobsitesentry.constant.NetworkConstants.SITE_ALARM_NOTIFICATION;
import static com.appdev360.jobsitesentry.constant.NetworkConstants.TIME_LAPSE_VIDEOS;
import static com.appdev360.jobsitesentry.constant.NetworkConstants.TOKEN_VALIDITY;
import static com.appdev360.jobsitesentry.constant.NetworkConstants.UPDATE_SETTING;
import static com.appdev360.jobsitesentry.constant.NetworkConstants.UPDATE_TOKEN;
import static com.appdev360.jobsitesentry.constant.NetworkConstants.USER_LOGOUT;
import static com.appdev360.jobsitesentry.constant.NetworkConstants.USER_UPDATE;


/**
 * Created by abubaker on 3/12/18.
 * <p>
 * Updated By Hussain Saad on 27/01/22
 */

public interface NetworkService {


    @POST(LOGIN_PATH)
    @FormUrlEncoded
    Observable<BaseModel> loginUser(@Field(EMAIL_PARAM) String email,
                                    @Field(PASSWORD_PARAM) String password,
                                    @Field(FCM_TOKEN) String fcmToken,
                                    @Field(TYPE) String type,
                                    @Field(BUILD_VERSION) String build_version);


    @POST(FORGOT_PASSWORD_PATH)
    @FormUrlEncoded
    Observable<BaseModel> forgotPassword(@Field(EMAIL_PARAM) String email);

    @POST(CHANGE_PASSWORD_PATH)
    @FormUrlEncoded
    Observable<BaseModel> changePassword(@Header(AUTHORIZATION_PARAM) String auth, @Field(PASSWORD_PARAM) String password,
                                         @Field(NEW_PASSWORD_PARAM) String newPassword);

    @POST(USER_UPDATE)
    @Multipart
    Observable<BaseModel> userUpdate(@Header(AUTHORIZATION_PARAM) String auth,@Part MultipartBody.Part image);

    @GET(LOCATION_LIST_PATH)
    Observable<BaseModel> sentryLocations(@Header(AUTHORIZATION_PARAM) String auth);

    @GET(DRONE_LIST_PATH)
    Observable<BaseModel> completeDroneList(@Header(AUTHORIZATION_PARAM) String auth);

    @POST(AGREEMENT_SIGN)
    @FormUrlEncoded
    Observable<AgreementModel> completeAgreement(@Header(AUTHORIZATION_PARAM) String auth,
                                                 @Field(TIME_ZONE) String timezone);

    @GET(ARREST_LIST_PATH)
    Observable<BaseModel> arrestVideos(@Header(AUTHORIZATION_PARAM) String auth);


    @GET(TOKEN_VALIDITY)
    Observable<TokenValidityGson> checkTokenValidity(@Header(AUTHORIZATION_PARAM) String auth);
    

    @POST(ALARM_VIDEO_LIST)
    @FormUrlEncoded
    Observable<BaseModel> alarmVideos(@Header(AUTHORIZATION_PARAM) String auth,
                                      @Field(UNIT_ID_PARAM) int unitId);


    @GET(LOCATION_UNITS)
    Observable<UnitDataMain> getUnits(@Header(AUTHORIZATION_PARAM) String auth,
                                      @Query(value="site_id", encoded=true) int unitId,@Query(value = "page", encoded=true) int page);


    @GET(TIME_LAPSE_VIDEOS)
    Observable<TimeLapseMain> getTimeLapseVideos(@Header(AUTHORIZATION_PARAM) String auth,
                                                 @Query(value="unit_id", encoded=true) int unitId, @Query(value = "camera_id", encoded=true) int cameraId, @Query(value = "page", encoded=true) int page);


    @POST(UPDATE_TOKEN)
    @FormUrlEncoded
    Observable<BaseModel> updateDeviceToken(@Header(AUTHORIZATION_PARAM) String auth,
                                            @Field(FCM_TOKEN) String fcmToken,
                                            @Field(TYPE) String type);


    @POST(USER_LOGOUT)
    Observable<BaseModel> logout(@Header(AUTHORIZATION_PARAM) String auth);

    @POST(UPDATE_SETTING)
    @FormUrlEncoded
    Observable<BaseModel> updateSetting(@Header(AUTHORIZATION_PARAM) String auth,
                                            @Field(ENABLED) boolean enabled,
                                            @Field(START_TIME) String startTime,
                                            @Field(END_TIME) String endTime);

    @POST(SITE_ALARM_NOTIFICATION)
    @FormUrlEncoded
    Observable<BaseModel> getAlarmNotification(@Header(AUTHORIZATION_PARAM) String auth,
                                            @Field(PAGE_NO) String pageNo);


    @GET
    Observable<ResponseBody> xaviorPtzCall(@Url String url);




    /******** Helper class that sets up a new services *******/
    class Creator {

        public static NetworkService networkService(@ApplicationContext Context context) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
//                    .addInterceptor(new AgreementAcceptInterceptor(context))
                    .connectTimeout(NetworkConstants.TIME_OUT, TimeUnit.SECONDS)
                    .readTimeout(NetworkConstants.TIME_OUT, TimeUnit.SECONDS)
                    .build();

            Gson gson = new GsonBuilder()
                    .setDateFormat(GeneralConstants.DEFAULT_TIME_FORMAT)
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build();
            return retrofit.create(NetworkService.class);
        }
    }
}
