package com.appdev360.jobsitesentry.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.widget.AgreementDialogListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AgreementDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private AgreementDialogListener agreementDialogListener;
    private AppCompatTextView titleAgr;
    private AppCompatTextView bodyAgr;
    private AppCompatButton cancelAgrBtn;
    private AppCompatButton agreeAgrBtn;
    private String title, body;

    public AgreementDialog(Context context, AgreementDialogListener agreementDialogListener, String title, String body) {
        super(context);
        this.context = context;
        this.agreementDialogListener = agreementDialogListener;
        this.title = title;
        this.body = body;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        setContentView(R.layout.dialog_agreement);
        setCancelable(true);

        titleAgr = findViewById(R.id.titleAgr);
        bodyAgr = findViewById(R.id.bodyAgr);
        cancelAgrBtn = findViewById(R.id.cancelAgrBtn);
        agreeAgrBtn = findViewById(R.id.agreeAgrBtn);

        titleAgr.setText(title);
        bodyAgr.setText(body);

        agreeAgrBtn.setOnClickListener(this);
        cancelAgrBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.agreeAgrBtn:
                agreementDialogListener.dialogAccept();
                dismiss();
                break;
            case R.id.cancelAgrBtn:
                agreementDialogListener.dialogDeny();
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}
