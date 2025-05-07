package com.appdev360.jobsitesentry.util;

import rx.Subscription;

public class RxUtil {

    public static void unSubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
