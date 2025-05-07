package com.appdev360.jobsitesentry.util;

import com.google.android.material.textfield.TextInputLayout;
import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * Created by abubaker on 3/12/18.
 */

public class FieldValidator {

    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX = "\\d{3}-\\d{7}";

    // Error Messages
    private static final String REQUIRED_MSG = "Required";
    private static final String EMAIL_MSG = "Invalid email";
    private static final String LENGTH_MSG = "Password must contain atleast six characters!";


    public static boolean isEmailAddress(EditText editText, TextInputLayout inputLayoutEmail, boolean required) {
        return isValid(editText, inputLayoutEmail,EMAIL_REGEX, EMAIL_MSG, required);
    }

    public static boolean isValid(EditText editText, TextInputLayout inputLayout, String regex, String errMsg, boolean required) {

        String text = editText.getText().toString().trim();
        inputLayout.setError(null);
        inputLayout.setErrorEnabled(true);
        if (required && !hasText(editText,inputLayout)) return false;
        if (required && !Pattern.matches(regex, text)) {
            inputLayout.setError(errMsg);
            return false;
        }
        inputLayout.setErrorEnabled(false);
        return true;
    }

    public static boolean hasText(EditText editText,TextInputLayout inputLayout) {

        String text = editText.getText().toString().trim();
        inputLayout.setError(null);
//        inputLayout.setErrorEnabled(true);
        if (text.length() == 0) {
            inputLayout.setError(REQUIRED_MSG);
            return false;
        } else {
            inputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public static boolean isLengthValid(EditText editText,TextInputLayout inputLayout, boolean required) {

        String text = editText.getText().toString().trim();
        inputLayout.setError(null);
        if (required && !hasText(editText,inputLayout)) return false;
        if (text.length() < 6) {
            inputLayout.setError(LENGTH_MSG);
            return false;
        }
        return true;
    }
}
