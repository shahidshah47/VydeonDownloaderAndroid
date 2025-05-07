package com.appdev360.jobsitesentry.ui.changepassword;

import android.content.Intent;
import android.os.Bundle;
import androidx.constraintlayout.widget.Guideline;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.ui.base.BaseActivity;
import com.appdev360.jobsitesentry.ui.base.BaseFragment;
import com.appdev360.jobsitesentry.ui.main.MainActivity;
import com.appdev360.jobsitesentry.ui.sentryLocation.SentryLocationActivity;
import com.appdev360.jobsitesentry.util.FieldValidator;
import com.appdev360.jobsitesentry.util.UtilSnackbar;
import com.appdev360.jobsitesentry.widget.TextViewCustom;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by abubaker on 3/21/18.
 */

public class ChangePassword extends BaseFragment implements ChangePasswordMvpView{

    @BindView(R.id.guideline)
    Guideline guideline;
    @BindView(R.id.label_change_password)
    TextViewCustom labelChangePassword;
    @BindView(R.id.et_current_password)
    TextInputEditText etCurrentPassword;
    @BindView(R.id.currentPasswordLayout)
    TextInputLayout currentPasswordLayout;
    @BindView(R.id.et_new_password)
    TextInputEditText etNewPassword;
    @BindView(R.id.newPasswordLayout)
    TextInputLayout newPasswordLayout;
    @BindView(R.id.et_confirm_password)
    TextInputEditText etConfirmPassword;
    @BindView(R.id.confirmPasswordLayout)
    TextInputLayout confirmPasswordLayout;
    @BindView(R.id.send_request)
    Button sendRequest;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    Unbinder unbinder;
    @Inject
    ChangePasswordPresenter presenter;
    private SentryLocationActivity mainActivity;

    @Override
    public void initViews(View parentView, Bundle saveInstanceState) {
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        unbinder = ButterKnife.bind(this, parentView);
        mainActivity = (SentryLocationActivity) getActivity();
        mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public int getLayoutId() {
        return R.layout.change_password_fragment_layout;
    }

    @Override
    public void updateFragmentReference() {
        unbinder = ButterKnife.bind(this, parent);
        presenter.attachView(this);
    }

    @Override
    public void updateActionState(boolean action, View v) {
        sendRequest.setEnabled(action);
        if (v != null) {
            v.setEnabled(!action);
        }
    }

    @OnClick({R.id.send_request})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.send_request:
                if (checkValidation()) {

                    if (FieldValidator.isLengthValid(etCurrentPassword, currentPasswordLayout, true) &&  FieldValidator
                            .isLengthValid(etNewPassword, newPasswordLayout, true) && FieldValidator
                            .isLengthValid(etConfirmPassword, confirmPasswordLayout, true)) {

                        if (etConfirmPassword.getText().toString().equals(etNewPassword.getText().toString())){
                            updateActionState(false, null);
                            progressBar.setVisibility(View.VISIBLE);
                            presenter.changePassword(etCurrentPassword.getText().toString(), etConfirmPassword.getText().toString
                                    ());
                        }else {
                            UtilSnackbar.showSnakbarError(parent,"Password does not match");
                        }

                    }
                }
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onPasswordChange(String message) {
        updateActionState(true, null);
        progressBar.setVisibility(View.GONE);
        UtilSnackbar.showSnakbarSuccess(parent, message);
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onError(Error error) {
        updateActionState(true, null);
        progressBar.setVisibility(View.GONE);
        UtilSnackbar.showSnakbarError(parent, error.getMessage());

    }

    private boolean checkValidation() {
        boolean ret = true;
        if (!FieldValidator.hasText(etCurrentPassword, currentPasswordLayout)) ret = false;
        if (!FieldValidator.hasText(etNewPassword, newPasswordLayout)) ret = false;
        if (!FieldValidator.hasText(etConfirmPassword, confirmPasswordLayout)) ret = false;
        return ret;
    }

    @Override
    public void onTokenExpire(Error error) {
      //  MyToast.showMessage(getActivity(),error.getMessage());
        dataManager.getPreferencesHelper().storeToken(null);
        dataManager.getPreferencesHelper().storeUserId(-1);
        dataManager.getPreferencesHelper().setImagePath(null);
        dataManager.getPreferencesHelper().storeUserName(null);
        dataManager.getPreferencesHelper().storeUserRole(null);
        getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }

    @Override
    public void onChangePasswrod(Error error) {

    }
}
