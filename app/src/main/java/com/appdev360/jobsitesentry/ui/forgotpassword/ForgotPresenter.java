package com.appdev360.jobsitesentry.ui.forgotpassword;

import android.content.Context;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.data.DataManager;
import com.appdev360.jobsitesentry.data.enums.ErrorType;
import com.appdev360.jobsitesentry.data.model.BaseModel;
import com.appdev360.jobsitesentry.data.model.Data;
import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.injection.ApplicationContext;
import com.appdev360.jobsitesentry.ui.base.BasePresenter;
import com.appdev360.jobsitesentry.util.GeneralUtils;
import com.appdev360.jobsitesentry.util.RxUtil;
import com.google.gson.Gson;

import javax.inject.Inject;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by abubaker on 3/12/18.
 */

public class ForgotPresenter extends BasePresenter<ForgotMvpView> {


    private final DataManager mDataManager;
    private Subscription mSubscription;
    Error defaultError;

    Context context;

    @Inject
    public ForgotPresenter(@ApplicationContext Context context, DataManager dataManager) {
        mDataManager = dataManager;
        this.context = context;
        defaultError = new Error();

    }

    @Override
    public void attachView(ForgotMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void forgotPassword(String email) {
        checkViewAttached();
        if (!GeneralUtils.isNetworkConnected(context)) {
            defaultError.setType(ErrorType.NETWORK);
            defaultError.setMessage(context.getString(R.string.network_error));
            getMvpView().onError(defaultError);
            return;
        }
        RxUtil.unSubscribe(mSubscription);
        mSubscription = mDataManager.getNetworkService().forgotPassword(email)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<BaseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getMvpView() == null || defaultError == null) {
                            return;
                        }
                        try {
                            Gson gson = new Gson();
                            BaseModel resp = gson.fromJson(((HttpException) e).response().errorBody().string(), BaseModel.class);
                            defaultError.setMessage(resp.getMessage());

                            if (resp.getStatusCode() == SUCCESS_HTTP_INVALID_TOKEN) {

                                getMvpView().onTokenExpire(defaultError);
                                return;

                            } else if (resp.getStatusCode() == SUCCESS_HTTP_TOKEN_EXPIRE) {

                                getMvpView().onTokenExpire(defaultError);
                                return;

                            }else if (resp.getStatusCode() == SUCCESS_HTTP_PASSWORD_EXPIRE) {

                                getMvpView().onChangePasswrod(defaultError);
                                return;

                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        getMvpView().onError(defaultError);
                    }

                    @Override
                    public void onNext(BaseModel resp) {
                        parseResponse(resp);
                    }
                });
    }

    private void parseResponse(BaseModel resp) {
        if (resp == null) {
            getMvpView().onError(defaultError);
            return;
        }
        if (resp.getStatusCode() != SUCCESS_HTTP_CODE) {
            if (resp.getMessage() != null && !resp.getMessage().equalsIgnoreCase("")) {
                defaultError.setMessage(resp.getMessage());
            }
            getMvpView().onError(defaultError);
            return;
        }
        Data data = resp.getData();
        if (data == null) {
            getMvpView().onError(defaultError);
            return;
        }

        getMvpView().onSuccess();
    }
}
