package com.appdev360.jobsitesentry.ui.camerastream;

import android.content.Context;
import android.os.StrictMode;

import com.albroco.barebonesdigest.DigestAuthentication;
import com.albroco.barebonesdigest.DigestChallengeResponse;
import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.data.DataManager;
import com.appdev360.jobsitesentry.data.enums.ErrorType;
import com.appdev360.jobsitesentry.data.local.PreferencesDataHelper;
import com.appdev360.jobsitesentry.data.model.BaseModel;
import com.appdev360.jobsitesentry.data.model.Data;
import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.data.model.unit.UnitData;
import com.appdev360.jobsitesentry.injection.ApplicationContext;
import com.appdev360.jobsitesentry.ui.base.BasePresenter;
import com.appdev360.jobsitesentry.util.GeneralUtils;
import com.appdev360.jobsitesentry.util.RxUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by abubaker on 3/13/18.
 */

public class CameraStreamPresenter extends BasePresenter<CameraSteamMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;
    Error defaultError;
    Context context;

    @Inject
    public CameraStreamPresenter(@ApplicationContext Context context, DataManager dataManager) {
        mDataManager = dataManager;
        this.context = context;
        defaultError = new Error();

    }

    @Override
    public void attachView(CameraSteamMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void doXaviorPtz(String url) {
        checkViewAttached();
        if (!GeneralUtils.isNetworkConnected(context)) {
            defaultError.setType(ErrorType.NETWORK);
            defaultError.setMessage(context.getString(R.string.network_error));
            getMvpView().onError(defaultError);
            return;
        }

        RxUtil.unSubscribe(mSubscription);
        mSubscription = mDataManager.getNetworkService().xaviorPtzCall(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().onPtzCallCompleted();

                    }

                    @Override
                    public void onNext(ResponseBody resp) {
                        getMvpView().onPtzCallCompleted();
                    }
                });
    }


    void doPtz(UnitData unitData, String channel, String code, int intCode) {

        try {
            if (unitData.getType().equalsIgnoreCase("xavier")) {

                String url ="";
//                http://ip:port/move-camera/channel/direction
//                http://ip:port/zoomin-camera/channel
//                http://ip:port/zoomout-camera/channel
                if(intCode==-2){
                    url="http://"+unitData.getIpAddress()+":"+unitData.getPort()+"/zoomin-camera/"+channel+"";
                }else if(intCode==-3){
                    url="http://"+unitData.getIpAddress()+":"+unitData.getPort()+"/zoomout-camera/"+channel+"";

                }else if (intCode==-4){
                     url="http://"+unitData.getIpAddress()+":"+unitData.getPort()+"/reset-camera/"+channel+"";

                }else {
                    url="http://"+unitData.getIpAddress()+":"+unitData.getPort()+"/move-camera/"+channel+"/"+intCode;

                }

                doXaviorPtz(url);
            } else {
                makeNetworkCall("start", unitData.getIpAddress(), channel, code, String.valueOf(unitData.getUsername()), String.valueOf(unitData.getPassword()));
//  testing camera
//  x("start", "166.167.220.158", "1", code, String.valueOf(unitData.getUsername()), String.valueOf(unitData.getPassword()));
            }
        } catch (IOException e) {
            getMvpView().onPtzCallCompleted();
            e.printStackTrace();
        }

    }

    void makeNetworkCall(String action, String ipAddress, String channel, String code, String userName, String password) throws IOException {

        Single<Boolean> single = Single.create(new Single.OnSubscribe<Boolean>() {
            @Override
            public void call(SingleSubscriber<? super Boolean> singleSubscriber) {
                try {

                    singleSubscriber.onSuccess(digest(action, ipAddress, channel, code, userName, password));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleSubscriber<Boolean>() {
            @Override
            public void onSuccess(Boolean value) {

                getMvpView().onPtzCallCompleted();
            }

            @Override
            public void onError(Throwable error) {
                getMvpView().onPtzCallCompleted();

            }
        });


    }

    private boolean digest(String action, String ipAddress, String channel, String code, String userName, String password) throws IOException {
        // Step 1. Create the connection
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
//        URL url = new URL("http://166.167.220.158/cgi-bin/ptz.cgi?action=start&channel=1&code=ZoomWide&arg1=0&arg2=5&arg3=0");
        URL url = new URL("http://" + ipAddress + "/cgi-bin/ptz.cgi?action=" + action + "&channel=" + channel + "&code=" + code + "&arg1=0&arg2=3&arg3=0");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        DigestAuthentication auth = DigestAuthentication.fromResponse(connection);
        // ...with correct credentials
//        auth.username(userName).password(password);
        auth.username("admin").password("Sentry_2020!");
        // Step 4 (Optional). Check if the challenge was a digest challenge of a supported type
        if (!auth.canRespond()) {
            // No digest challenge or a challenge of an unsupported type - do something else or fail
            return false;
        }

        // Step 5. Create a new connection, identical to the original one...
        connection = (HttpURLConnection) url.openConnection();
        // ...and set the Authorization header on the request, with the challenge response
        connection.setRequestProperty(DigestChallengeResponse.HTTP_HEADER_AUTHORIZATION,
                auth.getAuthorizationForRequest("GET", connection.getURL().getPath()));
// Step 2. Make the request and check to see if the response contains an authorization challenge
//        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//            // Step 3. Create a authentication object from the challenge...
//
        if (!action.equalsIgnoreCase("stop")) {
            makeNetworkCall("stop", ipAddress, channel, code, userName, password);
        }
//
//        }

        return (connection.getResponseCode() == HttpURLConnection.HTTP_OK);


    }
}
