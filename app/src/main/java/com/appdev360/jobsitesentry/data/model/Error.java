package com.appdev360.jobsitesentry.data.model;



import com.appdev360.jobsitesentry.data.enums.ErrorType;

import java.io.Serializable;

/**
 * Created by jaffarraza on 15/05/2017.
 */

public class Error implements Serializable {

    private int status;
    private int statusCode;
    private String message;
    private ErrorType type = ErrorType.NORMAL;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getHttpCode() {
        return statusCode;
    }

    public void setHttpCode(int httpCode) {
        this.statusCode = httpCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public ErrorType getType() {
        return type;
    }

    public void setType(ErrorType type) {
        this.type = type;
    }
}