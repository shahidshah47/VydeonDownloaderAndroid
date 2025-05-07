package com.appdev360.jobsitesentry.ui.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.app.ActivityCompat;

import com.appdev360.jobsitesentry.BuildConfig;
import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.data.DataManager;
import com.appdev360.jobsitesentry.constant.GeneralConstants;
import com.appdev360.jobsitesentry.data.model.Data;
import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.data.model.Event;
import com.appdev360.jobsitesentry.ui.agreement.AgreementActivity;
import com.appdev360.jobsitesentry.ui.base.BaseActivity;
import com.appdev360.jobsitesentry.ui.base.BaseFragment;
import com.appdev360.jobsitesentry.ui.forgotpassword.ForgotPasswordFragment;
import com.appdev360.jobsitesentry.ui.main.MainActivity;
import com.appdev360.jobsitesentry.ui.sentryLocation.SentryLocationActivity;
import com.appdev360.jobsitesentry.util.FieldValidator;
import com.appdev360.jobsitesentry.util.MyToast;
import com.appdev360.jobsitesentry.util.PermissionsHelper;
import com.appdev360.jobsitesentry.util.UtilSnackbar;
import com.appdev360.jobsitesentry.util.fingerprint.DeCryptor;
import com.appdev360.jobsitesentry.util.fingerprint.EnCryptor;
import com.appdev360.jobsitesentry.util.fingerprint.FingerPrintHelper;
import com.appdev360.jobsitesentry.util.fingerprint.FingerprintAuthenticationDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.yakivmospan.scytale.Crypto;
import com.yakivmospan.scytale.Options;
import com.yakivmospan.scytale.Store;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.appdev360.jobsitesentry.constant.GeneralConstants.AGREEMENT;
import static com.appdev360.jobsitesentry.constant.GeneralConstants.AGREEMENT_MSG;
import static com.appdev360.jobsitesentry.constant.GeneralConstants.PRIVACY;
import static com.appdev360.jobsitesentry.constant.GeneralConstants.TERMS_OF_USE;

/**
 * Created by abubaker on 3/12/18.
 * <p>
 * Updated By Hussain Saad on 27/01/22
 */

public class LoginFragment extends BaseFragment implements LoginMvpView {


    @BindView(R.id.guideline)
    Guideline guideline;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.et_email)
    TextInputEditText etEmail;
    @BindView(R.id.textInputLayout)
    TextInputLayout textInputLayout;
    @BindView(R.id.et_password)
    TextInputEditText etPassword;
    @BindView(R.id.passwordInputLayout)
    TextInputLayout passwordInputLayout;
    @BindView(R.id.button)
    Button loginButton;
    @BindView(R.id.forgot_password)
    TextView forgotPassword;
    Unbinder unbinder;
    @Inject
    LoginPresenter loginPresenter;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.btn_finger_print)
    TextView btnFingerPrint;
    @Inject
    DataManager dataManager;
    private MainActivity mainActivity;
    KeyStore mKeyStore;
    private EnCryptor encryptor;
    private DeCryptor decryptor;


    private static final String FINGER_PRINT_PERMISSION = "Manifest.permission.USE_FINGERPRINT";
    private static final String DIALOG_FRAGMENT_TAG = "myFragment";
    private static final String KEY_NAME_NOT_INVALIDATED = "key_not_invalidated";
    private Cipher cipherNotInvalidated;
    private Cipher mCipher;
    private Cipher defaultCipher;
    private KeyGenerator mKeyGenerator = null;

    @Override
    public void initViews(View parentView, Bundle savedInstanceState) {
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        unbinder = ButterKnife.bind(this, parentView);
        mainActivity = (MainActivity) getActivity();

        if (dataManager.getPreferencesHelper().isFingerPrintEnable()) {
            btnFingerPrint.setVisibility(View.VISIBLE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (FingerPrintHelper.checkFingerPrint(getActivity())) {

                    btnFingerPrint.setVisibility(View.VISIBLE);


                    try {
                        defaultCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                                + KeyProperties.BLOCK_MODE_CBC + "/"
                                + KeyProperties.ENCRYPTION_PADDING_PKCS7);
                        cipherNotInvalidated = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                                + KeyProperties.BLOCK_MODE_CBC + "/"
                                + KeyProperties.ENCRYPTION_PADDING_PKCS7);
                    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
                        throw new RuntimeException("Failed to get an instance of Cipher", e);
                    }

                } else {
                    btnFingerPrint.setVisibility(View.GONE);
                }
            }

        } else {
            btnFingerPrint.setVisibility(View.GONE);
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccess(Event event) {

        // Perform sign in
        updateActionState(false, null);
        progressBar.setVisibility(View.VISIBLE);

        String deCryptedEmail = null;
        String deCryptedPassword = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            Store store = new Store(getActivity().getApplicationContext());
            if (!store.hasKey(getActivity().getResources().getString(R.string.jobsitesentry))) {
                SecretKey key = store.generateSymmetricKey(getActivity().getResources().getString(R.string.jobsitesentry), null);
            }

            // Get key
            SecretKey key = store.getSymmetricKey(getActivity().getResources().getString(R.string.jobsitesentry), null);
            // Encrypt/Decrypt data
            Crypto crypto = new Crypto(Options.TRANSFORMATION_SYMMETRIC);

            deCryptedEmail = crypto.decrypt(dataManager.getPreferencesHelper().getEncryptedEmail(), key);
            deCryptedPassword = crypto.decrypt(dataManager.getPreferencesHelper().getEncryptedPassword(), key);


        }

        String versionName = BuildConfig.VERSION_NAME;
        Log.d("VERSION", "Version Number : " + versionName);
        loginPresenter.loginUser(deCryptedEmail, deCryptedPassword, versionName);

    }


    @Override
    public int getLayoutId() {
        return R.layout.sign_in_fragment_layout;
    }

    @Override
    public void updateFragmentReference() {
        unbinder = ButterKnife.bind(this, parent);
        loginPresenter.attachView(this);
    }

    @Override
    public void updateActionState(boolean action, View v) {
        loginButton.setEnabled(action);
        if (v != null) {
            v.setEnabled(!action);
        }
    }

    @OnClick({R.id.button, R.id.forgot_password, R.id.btn_finger_print})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button:
                if (checkValidation()) {

                    if (FieldValidator.isLengthValid(etPassword, passwordInputLayout, true)) {
                        // Perform sign in
                        updateActionState(false, null);
                        progressBar.setVisibility(View.VISIBLE);
                        String versionName = BuildConfig.VERSION_NAME;
                        Log.d("VERSION", "Version Number : " + versionName);
                        loginPresenter.loginUser(etEmail.getText().toString(), etPassword.getText().toString(), versionName);
                    }
                }
                break;
            case R.id.forgot_password:
                mainActivity.replaceFragment(new ForgotPasswordFragment());
                break;
            case R.id.btn_finger_print:


                if (PermissionsHelper.isExplicitPermissionsRequired()) {

                    if (PermissionsHelper.isPermissionGranted(getActivity(), Manifest.permission.USE_FINGERPRINT)) {

                        showDialog(defaultCipher);

                    } else {

                        PermissionsHelper.checkForPermission(getActivity(), Manifest.permission.USE_FINGERPRINT, GeneralConstants.FINGER_PRINT);

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
    public void onLogin(Data data) {
        updateActionState(true, null);
        progressBar.setVisibility(View.GONE);

        dataManager.getPreferencesHelper().storeToken(data.getUserToken());
        dataManager.getPreferencesHelper().storeUserId(data.getUserId());
        dataManager.getPreferencesHelper().storeUserName(data.getUserName());
        dataManager.getPreferencesHelper().storeUserThumb(data.getUserThumb());
        dataManager.getPreferencesHelper().storeUserRole(data.getUserRole());
        // Create and save key
        Store store = new Store(getActivity().getApplicationContext());
        if (!store.hasKey(getActivity().getResources().getString(R.string.jobsitesentry))) {
            SecretKey key = store.generateSymmetricKey(getActivity().getResources().getString(R.string.jobsitesentry), null);
        }

        // Get key
        SecretKey key = store.getSymmetricKey(getActivity().getResources().getString(R.string.jobsitesentry), null);
        // Encrypt/Decrypt data
        Crypto crypto = new Crypto(Options.TRANSFORMATION_SYMMETRIC);
        String encryptedEmail = crypto.encrypt(data.getUserEmail(), key);
        String password = Objects.requireNonNull(etPassword.getText()).toString();
        if (TextUtils.isEmpty(password))
            password = crypto.decrypt(dataManager.getPreferencesHelper().getEncryptedPassword(), key);
        String encryptedPassword = crypto.encrypt(password, key);
        dataManager.getPreferencesHelper().setEncryptedEmail(encryptedEmail);
        dataManager.getPreferencesHelper().setEncryptedPassword(encryptedPassword);


        UtilSnackbar.showSnakbarSuccess(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                "Welcome " + "" + dataManager.getPreferencesHelper().getUserName());

        progressBar.setVisibility(View.VISIBLE);
        loginPresenter.getAgreementInfo();

//        startActivity(new Intent(getActivity(), SentryLocationActivity.class));
//        mainActivity.finish();rizwan@nxvt.com
    }

    @Override
    public void onError(Error error) {
        updateActionState(true, null);
        progressBar.setVisibility(View.GONE);
        UtilSnackbar.showSnakbarError(parent, error.getMessage());
    }

    private boolean checkValidation() {
        boolean ret = true;
        if (!FieldValidator.isEmailAddress(etEmail, textInputLayout, true)) ret = false;
        if (!FieldValidator.hasText(etPassword, passwordInputLayout)) ret = false;
        return ret;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.checkSelfPermission(getActivity(), FINGER_PRINT_PERMISSION)
                    != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    showDialog(defaultCipher);
                }

            } else {

                // Permission Denied

                MyToast.showMessage(getActivity(), getResources().getString(R.string.some_permission_denied));

            }
        } else {

            // Permission Denied

            MyToast.showMessage(getActivity(), getResources().getString(R.string.some_permission_denied));

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showDialog(Cipher cipher) {
        mCipher = cipher;


        // Show the fingerprint dialog. The user has the option to use the fingerprint with
        // crypto, or you can fall back to using a server-side verified password.
        FingerprintAuthenticationDialogFragment fragment
                = new FingerprintAuthenticationDialogFragment();
        fragment.setStage(
                FingerprintAuthenticationDialogFragment.Stage.FINGERPRINT);

        fragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);

    }


    @Override
    public void onTokenExpire(Error error) {
        progressBar.setVisibility(View.GONE);
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
        progressBar.setVisibility(View.GONE);
        Intent intent = new Intent(getActivity(), SentryLocationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("password", "password");
        intent.putExtras(bundle);
        startActivity(intent);
        mainActivity.finish();
    }

    @Override
    public void onAgreementRequire(String agreement, String terms_of_use, String privay) {
        progressBar.setVisibility(View.GONE);

//        new AgreementDialog(getActivity(), new AgreementDialogListener() {
//            @Override
//            public void dialogAccept() {
//                Intent agreementIntent = new Intent(getActivity(), AgreementActivity.class);
//                agreementIntent.putExtra(AGREEMENT, agreement);
//                agreementIntent.putExtra(TERMS_OF_USE, terms_of_use);
//                agreementIntent.putExtra(PRIVACY, privay);
//                startActivity(agreementIntent);
//                mainActivity.finish();
//            }
//            @Override
//            public void dialogDeny() {
//
//            }
//        }, mainActivity.getResources().getString(R.string.titleAgre),
//                mainActivity.getResources().getString(R.string.bodyAgre)).show();

        Intent agreementIntent = new Intent(getActivity(), AgreementActivity.class);
        agreementIntent.putExtra(AGREEMENT, agreement);
        agreementIntent.putExtra(TERMS_OF_USE, terms_of_use);
        agreementIntent.putExtra(PRIVACY, privay);
        startActivity(agreementIntent);
        mainActivity.finish();

    }

    @Override
    public void onAgreementOk() {
        progressBar.setVisibility(View.GONE);
        startActivity(new Intent(getActivity(), SentryLocationActivity.class));
        mainActivity.finish();
    }
}
