package com.appdev360.jobsitesentry.ui.base;

/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 */
public class BasePresenter<T extends MvpView> implements Presenter<T> {

    public static int DEFAULT_HTTP_CODE = 400;
    public static int SUCCESS_HTTP_CODE = 1;
    public static int SUCCESS_HTTP_INVALID_TOKEN = 10;
    public static int SUCCESS_HTTP_TOKEN_EXPIRE = 11;
    public static int SUCCESS_HTTP_TOKEN_REQUIRED = 12;
    public static int SUCCESS_HTTP_AGREEMENT_REQUIRED = 14;

    public static int SUCCESS_HTTP_PASSWORD_EXPIRE = 13;
    public static String DEFAULT_ERROR_MSG = "An error occurred. Please try again.";

    private T mMvpView;

    @Override
    public void attachView(T mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public T getMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }
}

