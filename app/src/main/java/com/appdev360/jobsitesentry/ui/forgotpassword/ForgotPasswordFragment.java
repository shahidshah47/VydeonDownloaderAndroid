package com.appdev360.jobsitesentry.ui.forgotpassword;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.ui.base.BaseActivity;
import com.appdev360.jobsitesentry.ui.base.BaseFragment;
import com.appdev360.jobsitesentry.ui.login.LoginFragment;
import com.appdev360.jobsitesentry.ui.main.MainActivity;
import com.appdev360.jobsitesentry.util.FieldValidator;
import com.appdev360.jobsitesentry.util.UtilSnackbar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by abubaker on 3/9/18.
 */

public class ForgotPasswordFragment extends BaseFragment implements ForgotMvpView {

    @BindView(R.id.et_email)
    TextInputEditText etEmail;
    @BindView(R.id.textInputLayout)
    TextInputLayout textInputLayout;
    @BindView(R.id.send_request)
    Button sendRequest;
    @BindView(R.id.sign_in)
    TextView signIn;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    Unbinder unbinder;
    @Inject
    ForgotPresenter forgotPresenter;
    private MainActivity mainActivity;

    @Override
    public void initViews(View parentView, Bundle savedInstanceState) {
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        unbinder = ButterKnife.bind(this, parentView);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public int getLayoutId() {
        return R.layout.forgot_password_fragment_layout;
    }

    @Override
    public void updateFragmentReference() {
        unbinder = ButterKnife.bind(this, parent);
        forgotPresenter.attachView(this);

    }

    @Override
    public void updateActionState(boolean action, View v) {
        sendRequest.setEnabled(action);
        if (v != null) {
            v.setEnabled(!action);
        }
    }


    @OnClick({R.id.send_request, R.id.sign_in})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.send_request:
                if (checkValidation()) {

                    if (FieldValidator.isLengthValid(etEmail, textInputLayout, true)) {
                        updateActionState(false, null);
                        progressBar.setVisibility(View.VISIBLE);
                        forgotPresenter.forgotPassword(etEmail.getText().toString());
                    }
                }
                break;
            case R.id.sign_in:
                mainActivity.replaceFragment(new LoginFragment());
                break;

        }
    }

    @Override
    public void onSuccess() {
        updateActionState(true, null);
        progressBar.setVisibility(View.GONE);
        UtilSnackbar.showSnakbarSuccess(parent, getActivity().getResources().getString(R.string.password_reset_instruction));
        mainActivity.replaceFragment(new LoginFragment());

    }

    @Override
    public void onError(Error error) {
        updateActionState(true, null);
        progressBar.setVisibility(View.GONE);
        UtilSnackbar.showSnakbarError(parent, error.getMessage());
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

    private boolean checkValidation() {
        boolean ret = true;
        if (!FieldValidator.isEmailAddress(etEmail, textInputLayout, true)) ret = false;
        return ret;
    }

    @Override
    public void onTokenExpire(Error error) {
       // MyToast.showMessage(getActivity(),error.getMessage());
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
