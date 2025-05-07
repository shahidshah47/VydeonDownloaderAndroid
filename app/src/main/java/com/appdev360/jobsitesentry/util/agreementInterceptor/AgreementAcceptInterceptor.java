package com.appdev360.jobsitesentry.util.agreementInterceptor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.appdev360.jobsitesentry.constant.NetworkConstants;
import com.appdev360.jobsitesentry.injection.ApplicationContext;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public final class AgreementAcceptInterceptor implements Interceptor {

    private Context mContext;

    public AgreementAcceptInterceptor(@ApplicationContext Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        String rawRes = response.body().string();

        JSONObject obj = null;
        try {
            obj = new JSONObject(rawRes);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assert obj != null;
        Log.d("My App", obj.toString());


        if (response.code() == 401) {
            Intent intent=new Intent(NetworkConstants.ACTION_LOGOUT);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        }

        return response;
    }
}