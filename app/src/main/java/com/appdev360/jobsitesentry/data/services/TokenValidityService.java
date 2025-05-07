package com.appdev360.jobsitesentry.data.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.net.Network;
import android.util.Log;

import com.appdev360.jobsitesentry.data.local.PreferencesDataHelper;
import com.appdev360.jobsitesentry.data.model.token_validity.TokenValidityGson;
import com.appdev360.jobsitesentry.data.remote.NetworkService;
import com.appdev360.jobsitesentry.util.EventBusMessage;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.appdev360.jobsitesentry.ui.base.BasePresenter.SUCCESS_HTTP_INVALID_TOKEN;
import static com.appdev360.jobsitesentry.ui.base.BasePresenter.SUCCESS_HTTP_PASSWORD_EXPIRE;
import static com.appdev360.jobsitesentry.ui.base.BasePresenter.SUCCESS_HTTP_TOKEN_EXPIRE;
import static com.appdev360.jobsitesentry.util.EventBusMessage.TOKEN_EXPIRED;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */

public class TokenValidityService extends IntentService {


    private Subscription mSubscription;

    public TokenValidityService() {
        super("TokenValidityService");
    }

    public static void startTokenValidity(Context context) {
        Intent intent = new Intent(context, TokenValidityService.class);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            makeTokenValidityRequest();
        }
    }

    public void makeTokenValidityRequest() {

        String authToken = PreferencesDataHelper.retrieve(getApplicationContext(), PreferencesDataHelper.PersistenceKey.TOKEN);
        if (authToken == null || authToken.length() < 1) {
            Log.i("Test321", "Auth Token Null - Cancelling call");
            return;
        }

        Log.i("Test321", "Auth Token not null");


        PreferencesDataHelper.retrieve(getApplicationContext(), PreferencesDataHelper.PersistenceKey.LOGGEDIN_USER);
        NetworkService networkService = NetworkService.Creator.networkService(getApplicationContext());

        mSubscription = networkService.checkTokenValidity("bearer" + authToken)
                .subscribe(new Subscriber<TokenValidityGson>() {
                    @Override
                    public void onCompleted() {
                        Log.i("Test321", "Completed Token Validity Request");
                    }

                    @Override
                    public void onError(Throwable e) {
                        // try {
                        if (e instanceof HttpException) {
                            try {
                                Gson gson = new Gson();
                                TokenValidityGson resp = gson.fromJson(((HttpException) e).response().errorBody().string(), TokenValidityGson.class);
                                if (resp.getStatusCode() == SUCCESS_HTTP_INVALID_TOKEN) {
                                    Log.i("Test321", "Invalid Token");
                                    return;

                                } else if (resp.getStatusCode() == SUCCESS_HTTP_TOKEN_EXPIRE) {
                                    Log.i("Test321", "Token expired");
                                    EventBus.getDefault().post(new EventBusMessage(TOKEN_EXPIRED));
                                    return;

                                } else if (resp.getStatusCode() == SUCCESS_HTTP_PASSWORD_EXPIRE) {
                                    return;
                                }
                            } catch (Exception e1) {
                                Log.i("Test321", "Exception Token validity" + e1.getMessage());
                                e1.printStackTrace();
                            }

                        } else {
                            Log.i("Test321", "An Unknown Error occurred");
                        }
                        return;

                    }

                    @Override
                    public void onNext(TokenValidityGson resp) {
                        Log.i("Test321", "Token is valid");

                    }
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Log.i("Test321", "onDestroy Called");
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
