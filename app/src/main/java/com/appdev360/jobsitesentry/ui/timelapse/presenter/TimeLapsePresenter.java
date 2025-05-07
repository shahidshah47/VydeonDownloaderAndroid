package com.appdev360.jobsitesentry.ui.timelapse.presenter;

import android.content.Context;
import android.util.Log;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.data.DataManager;
import com.appdev360.jobsitesentry.data.enums.ErrorType;
import com.appdev360.jobsitesentry.data.local.PreferencesDataHelper;
import com.appdev360.jobsitesentry.data.model.BaseModel;
import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.data.model.timelapse.TimeLapseMain;
import com.appdev360.jobsitesentry.data.model.unit.Meta;
import com.appdev360.jobsitesentry.data.model.unit.UnitDataMain;
import com.appdev360.jobsitesentry.injection.ApplicationContext;
import com.appdev360.jobsitesentry.ui.base.BasePresenter;
import com.appdev360.jobsitesentry.ui.sentryunit.SentryUnitMvpView;
import com.appdev360.jobsitesentry.ui.timelapse.view.TimeLapseMvpView;
import com.appdev360.jobsitesentry.util.GeneralUtils;
import com.appdev360.jobsitesentry.util.RxUtil;
import com.google.gson.Gson;

import javax.inject.Inject;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TimeLapsePresenter extends BasePresenter<TimeLapseMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;
    Error defaultError;
    Context context;
    private boolean isLoading = false;
    private int nextPage = 2;
    private int currentPage = 1;
    private int unitId = 0;


    private int cameraId = 0;

    @Inject
    public TimeLapsePresenter(@ApplicationContext Context context, DataManager dataManager) {
        mDataManager = dataManager;
        this.context = context;
        defaultError = new Error();
    }

    @Override
    public void attachView(TimeLapseMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }


    public void getTimeLapVideos(int unitId, int cameraId,int page) {
        checkViewAttached();
        if (!GeneralUtils.isNetworkConnected(context)) {
            defaultError.setType(ErrorType.NETWORK);
            defaultError.setMessage(context.getString(R.string.network_error));
            getMvpView().onError(defaultError);
            return;
        }

        RxUtil.unSubscribe(mSubscription);
        String authToken = PreferencesDataHelper.retrieve(context, PreferencesDataHelper.PersistenceKey.TOKEN);
        mSubscription = mDataManager.getNetworkService().getTimeLapseVideos("bearer" + authToken, unitId, cameraId,page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<TimeLapseMain>() {
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

                            } else if (resp.getStatusCode() == SUCCESS_HTTP_PASSWORD_EXPIRE) {

                                getMvpView().onChangePasswrod(defaultError);
                                return;

                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        defaultError.setMessage(e.getMessage());
                        getMvpView().onError(defaultError);
                    }

                    @Override
                    public void onNext(TimeLapseMain resp) {
                        parseResponse(resp);
                    }
                });
    }

    public void loadNextPage() {
        if (hasNextPage()) {
            getMvpView().addLoadingRow();
            Log.i("Test321", "loadNextPage - unit id = " + unitId + " nextPage = " + nextPage);
            getTimeLapVideos(unitId,cameraId, nextPage);
        } else
        {
            Log.i("Test321", "Has next page false : current page ="+currentPage+" next page = "+nextPage);
        }
    }

    public boolean hasNextPage() {
        if (currentPage < nextPage)
            return true;

        return false;
    }

    private void parseResponse(TimeLapseMain resp) {
        Log.i("Test321","TimeLaps response size = "+resp.getData().getTimeLapseData().size());
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


        Meta meta = resp.getData().getMeta();
        currentPage = meta.getCurrentPage();

        int one = 1;
        if (meta.getCurrentPage() == one) {
            Log.i("Test321", "TimeLapse show first page ");

            getMvpView().showFirstPage(resp.getData());
        } else {
            getMvpView().appendNextItems(resp.getData());
        }

        //Check if has next page
        if (currentPage < meta.getTotalPages()) {
            nextPage = currentPage+1;
        } else {
            Log.i("Test321", "pages over");
        }
    }


    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

}
