package com.appdev360.jobsitesentry.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import com.appdev360.jobsitesentry.R;


public final class DialogFactory {

    public static Dialog createSimpleOkErrorDialog(Context context, String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }

    public static Dialog createSimpleOkErrorDialog(Context context,
                                                   @StringRes int titleResource,
                                                   @StringRes int messageResource) {

        return createSimpleOkErrorDialog(context,
                context.getString(titleResource),
                context.getString(messageResource));
    }

    public static Dialog createGenericErrorDialog(Context context, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.dialog_error_title))
                .setMessage(message)
                .setNeutralButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }

    public static Dialog createGenericErrorDialog(Context context, @StringRes int messageResource) {
        return createGenericErrorDialog(context, context.getString(messageResource));
    }

    public static ProgressDialog createProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        return progressDialog;
    }

    public static ProgressDialog createProgressDialog(Context context,
                                                      @StringRes int messageResource) {
        return createProgressDialog(context, context.getString(messageResource));
    }

    public static Dialog createConfirmationDialog(Context context,
                                                  @StringRes int messageResource,
                                                  @StringRes int txtPositiveButton,
                                                  DialogInterface.OnClickListener positiveListener,
                                                  DialogInterface.OnClickListener negativeListener) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setMessage(context.getString(messageResource))
                .setPositiveButton(context.getString(txtPositiveButton), positiveListener)
                .setNegativeButton(R.string.dialog_cancel, negativeListener);
        return alertDialog.create();
    }

    public static Dialog createConfirmationDialog(Context context,
                                                  String title,
                                                  String message,
                                                  DialogInterface.OnClickListener positiveListener,
                                                  DialogInterface.OnClickListener negativeListener) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.view, positiveListener)
                .setNegativeButton(R.string.cancel, negativeListener);
        return alertDialog.create();
    }


    public static Dialog createNotificationDialog(Context context,
                                                  String title,
                                                  String message,
                                                  DialogInterface.OnClickListener positiveListener,
                                                  DialogInterface.OnClickListener negativeListener) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, positiveListener)
                .setNegativeButton(R.string.cancel, negativeListener);
        return alertDialog.create();
    }

}

