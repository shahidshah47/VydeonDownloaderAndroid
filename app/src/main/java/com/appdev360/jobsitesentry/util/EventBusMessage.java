package com.appdev360.jobsitesentry.util;


public class EventBusMessage {


    public static String EVENT_ON_BACK_PRESSED = "backPressed";
    public static String TOKEN_EXPIRED = "TokenExpired";
    public static String EVENT_FIREBASE_MESSAGE = "firebaseMessage";


    public final String eventType;
    public final String message;


    public EventBusMessage(String eventType) {
        this.eventType = eventType;
        message = "";
    }

    public EventBusMessage(String eventType, String message) {
        this.eventType = eventType;
        this.message = message;
    }


}
