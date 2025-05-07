package com.appdev360.jobsitesentry.ui.setting;

import android.content.Context;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.data.DataManager;
import com.appdev360.jobsitesentry.data.enums.ErrorType;
import com.appdev360.jobsitesentry.data.local.PreferencesDataHelper;
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
 * Created by Abubaker on 04,May,2018
 */
public class LogoutPresenter extends BasePresenter<LogoutMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;
    Error defaultError;
    Context context;

    @Inject
    public LogoutPresenter(@ApplicationContext Context context, DataManager dataManager) {
        mDataManager = dataManager;
        this.context = context;
        defaultError = new Error();

    }

    @Override
    public void attachView(LogoutMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }


    public void logout() {
        checkViewAttached();
        if (!GeneralUtils.isNetworkConnected(context)) {
            defaultError.setType(ErrorType.NETWORK);
            defaultError.setMessage(context.getString(R.string.network_error));
            getMvpView().onLogoutError(defaultError);
            return;
        }
        RxUtil.unSubscribe(mSubscription);
        String authToken = PreferencesDataHelper.retrieve(context, PreferencesDataHelper.PersistenceKey.TOKEN);
        mSubscription = mDataManager.getNetworkService().logout("Bearer "+authToken)
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

                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        defaultError.setMessage(e.getMessage());
                        getMvpView().onLogoutError(defaultError);
                    }

                    @Override
                    public void onNext(BaseModel resp) {
                        parseResponse(resp);
                    }
                });
    }

    private void parseResponse(BaseModel resp) {
        if (resp == null) {
            getMvpView().onLogoutError(defaultError);
            return;
        }
        if (resp.getStatusCode() != SUCCESS_HTTP_CODE) {
            if (resp.getMessage() != null && !resp.getMessage().equalsIgnoreCase("")) {
                defaultError.setMessage(resp.getMessage());
            }
            getMvpView().onLogoutError(defaultError);
            return;
        }
        Data data = resp.getData();
        if (data == null) {
            getMvpView().onLogoutError(defaultError);
            return;
        }


        getMvpView().onLogout(resp.getMessage());
    }

}