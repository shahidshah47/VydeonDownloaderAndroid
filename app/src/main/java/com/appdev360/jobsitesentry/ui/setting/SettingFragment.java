package com.appdev360.jobsitesentry.ui.setting;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.appdev360.jobsitesentry.BuildConfig;
import com.appdev360.jobsitesentry.R;
import com.appdev360.jobsitesentry.data.DataManager;
import com.appdev360.jobsitesentry.constant.GeneralConstants;
import com.appdev360.jobsitesentry.data.model.Data;
import com.appdev360.jobsitesentry.data.model.Error;
import com.appdev360.jobsitesentry.ui.base.BaseActivity;
import com.appdev360.jobsitesentry.ui.base.BaseFragment;
import com.appdev360.jobsitesentry.ui.changepassword.ChangePassword;
import com.appdev360.jobsitesentry.ui.main.MainActivity;
import com.appdev360.jobsitesentry.ui.notificationsetting.NotificationMvpView;
import com.appdev360.jobsitesentry.ui.notificationsetting.NotificationPresenter;
import com.appdev360.jobsitesentry.ui.sentryLocation.SentryLocationActivity;
import com.appdev360.jobsitesentry.ui.webview.WebViewFragment;
import com.appdev360.jobsitesentry.util.DialogFactory;
import com.appdev360.jobsitesentry.util.GeneralUtils;
import com.appdev360.jobsitesentry.util.ImageUtils;
import com.appdev360.jobsitesentry.util.MyToast;
import com.appdev360.jobsitesentry.util.PermissionsHelper;
import com.appdev360.jobsitesentry.util.UtilSnackbar;
import com.appdev360.jobsitesentry.widget.TextViewCustom;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.squareup.picasso.Picasso;
import com.yakivmospan.scytale.Crypto;
import com.yakivmospan.scytale.Options;
import com.yakivmospan.scytale.Store;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by abubaker on 3/12/18.
 * <p>
 * Updated By Hussain Saad on 27/01/22
 */

public class SettingFragment extends BaseFragment implements SettingMvpView, NotificationMvpView, LogoutMvpView {

    Unbinder unbinder;
    @BindView(R.id.profile_pic)
    CircleImageView profilePic;
    @BindView(R.id.user_name)
    TextViewCustom userName;
    @BindView(R.id.user_email)
    TextViewCustom userEmail;
    @BindView(R.id.profile_layout)
    RelativeLayout profileLayout;
    @BindView(R.id.change_avatar)
    TextViewCustom changeAvatar;
    @BindView(R.id.change_password)
    TextViewCustom changePassword;
    @BindView(R.id.change_profile_layout)
    RelativeLayout changeProfileLayout;
    @BindView(R.id.about_us)
    TextViewCustom aboutUs;
    @BindView(R.id.privacy_policy)
    TextViewCustom privacyPolicy;
    @BindView(R.id.terms_of_service)
    TextViewCustom termsOfService;
    @BindView(R.id.feedback)
    TextViewCustom feedback;
    @BindView(R.id.build_info)
    TextViewCustom buildInfo;
    @BindView(R.id.link_layout)
    RelativeLayout linkLayout;
    @BindView(R.id.logout)
    TextViewCustom logout;
    @BindView(R.id.logout_layout)
    RelativeLayout logoutLayout;
    @BindView(R.id.finger_print_id)
    TextViewCustom fingerPrintId;
    /*   @BindView(R.id.txt_notification)
       TextViewCustom txtNotification;*/
    @BindView(R.id.checked)
    ImageView checkedIcon;
    /*   @BindView(R.id.notification_checked)
       ImageView imgNotification;*/
    @BindView(R.id.view_notification)
    View view;
    private SentryLocationActivity activity;
    @Inject
    DataManager dataManager;
    @Inject
    SettingPresenter presenter;
    @Inject
    NotificationPresenter notificationPresenter;
    @Inject
    LogoutPresenter logoutPresenter;

    private boolean isScreenVisible;

    private static final List<String> permissions = Arrays.asList(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    );

    private static final List<String> galleryPermissions = Arrays.asList(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    );

    @Override
    public void initViews(View parentView, Bundle savedInstanceState) {
        ((BaseActivity) getActivity()).activityComponent().inject(this);
        activity = (SentryLocationActivity) getActivity();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(getActivity());


//                // Device doesn't support fingerprint authentication
//            } else if (!fingerprintManagerCompat.hasEnrolledFingerprints()) {
//                // User hasn't enrolled any fingerprints to authenticate with
//            } else {
//                // Everything is ready for fingerprint authentication
//
//            }


            // if (FingerPrintHelper.checkFingerPrint(getActivity())) {
            if (fingerprintManagerCompat.isHardwareDetected()) {
                fingerPrintId.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);

                if (dataManager.getPreferencesHelper().isFingerPrintEnable()) {
                    checkedIcon.setVisibility(View.VISIBLE);
                } else {
                    checkedIcon.setVisibility(View.GONE);
                }


            } else {
                fingerPrintId.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
                checkedIcon.setVisibility(View.GONE);
            }
        } else {
            fingerPrintId.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            checkedIcon.setVisibility(View.GONE);
        }


     /*   if (dataManager.getPreferencesHelper().isNotificationOn()){

            imgNotification.setVisibility(View.VISIBLE);
        }else {
            imgNotification.setVisibility(View.GONE);
        }
*/

        if (dataManager.getPreferencesHelper().getUserName() != null && !dataManager.getPreferencesHelper().getUserName().equals("null")) {
            userName.setText(dataManager.getPreferencesHelper().getUserName());
        }

        // Get key
        Store store = new Store(getActivity().getApplicationContext());
        SecretKey key = store.getSymmetricKey(getActivity().getResources().getString(R.string.jobsitesentry), null);
        // Encrypt/Decrypt data
        Crypto crypto = new Crypto(Options.TRANSFORMATION_SYMMETRIC);

        if (key != null && !key.equals("")) {
            String decryptEmail = crypto.decrypt(dataManager.getPreferencesHelper().getEncryptedEmail(), key);
            userEmail.setText(decryptEmail);

        }


        if (dataManager.getPreferencesHelper().getUserThumb() != null) {
            Picasso.get().load(dataManager.getPreferencesHelper().getUserThumb()).resize(100, 100).centerCrop()
                    .into(profilePic);
        }

        String version = BuildConfig.VERSION_NAME + '(' + BuildConfig.VERSION_CODE + ')';
        buildInfo.setText(getString(R.string.build_info, version));
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_setting_layout;
    }

    @Override
    public void updateFragmentReference() {
        presenter.attachView(this);
        notificationPresenter.attachView(this);
        logoutPresenter.attachView(this);
    }

    @Override
    public void updateActionState(boolean action, View v) {

    }


    @OnClick(R.id.change_avatar)
    public void changeProfilePic() {
        isScreenVisible = true;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) openImagePickerAndroid12();
        else dialogOption();
    }

    private void openImagePickerAndroid12() {
        ImagePicker.with(SettingFragment.this)
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start();
    }

    @OnClick({R.id.logout, R.id.about_us, R.id.privacy_policy, R.id.terms_of_service, R.id.feedback, R.id
            .change_password, R.id.finger_print_id})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.logout:

                logoutPresenter.logout();

                break;
            case R.id.about_us:
                launchWebView(GeneralConstants.ABOUT_US, "About Us");
                break;
            case R.id.privacy_policy:
                launchWebView(GeneralConstants.PRIVACY_POLICY, "Privacy Policy");
                break;
            case R.id.terms_of_service:
                launchWebView(GeneralConstants.TERMS_OF_SERVICES, "Terms of Services");
                break;
            case R.id.feedback:
                launchWebView(GeneralConstants.FEEDBACK, "Feedback");
                break;
            case R.id.change_password:
                activity.addFragment(new ChangePassword(), "Change Password");
                break;
            case R.id.finger_print_id:

                if (dataManager.getPreferencesHelper().isFingerPrintEnable()) {

                    disableFingerPrintDialog();

                } else {
                    enableFingerPrintDialog();
                }
                break;

         /*   case R.id.txt_notification:

                if (dataManager.getPreferencesHelper().isNotificationOn()){
                    notificationDialog();
                    return;
                }

                activity.addFragment(new NotificationSetting(),"Notification Setting");

                break;*/


        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

  /*  @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Success success) {
        dataManager.getPreferencesHelper().setNotificationOn(true);
        imgNotification.setVisibility(View.VISIBLE);

    }*/

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


    private void dialogOption() {
        final CharSequence[] items = {"Take Photo", "Choose Photo", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int item) {

                if (items[item].equals("Take Photo")) {


                    if (PermissionsHelper.isExplicitPermissionsRequired()) {


                        if (PermissionsHelper.arePermissionGranted(getActivity(), permissions)) {

                            GeneralUtils.dispatchTakePictureIntent(getActivity());

                        } else {
                            isScreenVisible = true;
                            PermissionsHelper.checkForMultiplePermission(getActivity(), GeneralConstants.TAKE_PHOTO_CODE,
                                    permissions);

                        }

                    } else {

                        GeneralUtils.dispatchTakePictureIntent(getActivity());

                    }

                } else if (items[item].equals("Choose Photo")) {


                    if (PermissionsHelper.isExplicitPermissionsRequired()) {

                        if (PermissionsHelper.arePermissionGranted(getActivity(), galleryPermissions)) {

                            GeneralUtils.readFileFromGallery(getActivity());

                        } else {
                            isScreenVisible = true;
                            PermissionsHelper.checkForMultiplePermission(getActivity(), GeneralConstants.SELECT_FILE,
                                    galleryPermissions);
                        }


                    } else {

                        GeneralUtils.readFileFromGallery(getActivity());

                    }

                } else if (items[item].equals("Cancel")) {
                    d.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case GeneralConstants.TAKE_PHOTO_CODE: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for Camera Permission
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                    GeneralUtils.dispatchTakePictureIntent(getActivity());
                    isScreenVisible = false;

                } else {
                    // Permission Denied

                    MyToast.showMessage(getActivity(), getResources().getString(R.string.some_permission_denied));
                }
            }
            break;

            case GeneralConstants.SELECT_FILE: {

                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for Camera Permission
                if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                    GeneralUtils.readFileFromGallery(getActivity());

                    isScreenVisible = false;


                } else {
                    // Permission Denied

                    MyToast.showMessage(getActivity(), getResources().getString(R.string.some_permission_denied));
                }


            }

            break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                Uri uri = data.getData();
                profilePic.setImageURI(uri);

                isScreenVisible = false;
                File file = new File(uri.getPath());
                presenter.updateProfile(file);

            } else {

                if (requestCode == GeneralConstants.SELECT_FILE) {

                    profilePic.setImageBitmap(getBitmapFromGalleryResult(getImagePath(data)));
                    isScreenVisible = false;

                    File file = new File(getImagePath(data));
                    presenter.updateProfile(file);

                } else if (requestCode == GeneralConstants.TAKE_PHOTO_CODE) {

                    dataManager.getPreferencesHelper().setImagePath(GeneralUtils.getCurrentPhotoPath());
                    ImageUtils.loadImageLocally(getActivity(), GeneralUtils.getCurrentPhotoPath(), 100, 100, profilePic);
                    isScreenVisible = false;

                    File file = new File(GeneralUtils.getCurrentPhotoPath());
                    presenter.updateProfile(file);
                }
            }

        }
    }


    private Bitmap getBitmapFromGalleryResult(String selectedImagePath) {

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);
        return bm;
    }

    private void launchWebView(String link, String tag) {
        WebViewFragment fragment = new WebViewFragment();
        fragment.setUrl(link);
        activity.addFragment(fragment, tag);
    }

    private String getImagePath(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getActivity().managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);
        dataManager.getPreferencesHelper().setImagePath(selectedImagePath);

        return selectedImagePath;
    }

    private void enableFingerPrintDialog() {
        Dialog dialog = DialogFactory.createConfirmationDialog(getActivity(), (R.string.fingerprint_enable_message),
                R.string.dialog_action_enable,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        checkedIcon.setVisibility(View.VISIBLE);
                        dataManager.getPreferencesHelper().enableFingerPrint(true);

                    }
                }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void disableFingerPrintDialog() {


        Dialog dialog = DialogFactory.createConfirmationDialog(getActivity(), (R.string.fingerprint_disable_message),
                R.string.dialog_action_disable,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        checkedIcon.setVisibility(View.GONE);
                        dataManager.getPreferencesHelper().enableFingerPrint(false);

                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

        dialog.show();
    }

    @Override
    public void onUpdateProfile(Data data) {

        dataManager.getPreferencesHelper().storeToken(data.getUserToken());
        dataManager.getPreferencesHelper().storeUserId(data.getUserId());
        dataManager.getPreferencesHelper().storeUserName(data.getUserName());
        dataManager.getPreferencesHelper().storeUserThumb(data.getUserThumb());
        dataManager.getPreferencesHelper().storeEmail(data.getUserEmail());

    }

    @Override
    public void onSuccess(String message) {

        dataManager.getPreferencesHelper().setNotificationOn(false);
        //imgNotification.setVisibility(View.GONE);
    }

    @Override
    public void onNotificationError(Error error) {
        UtilSnackbar.showSnakbarError(parent, error.getMessage());
    }

    @Override
    public void onProfileError(Error error) {

        UtilSnackbar.showSnakbarError(parent, error.getMessage());
    }

    private void notificationDialog() {
        Dialog dialog = DialogFactory.createNotificationDialog(getActivity(), "Notification Setting", "Do you want " +
                "to turn off the notification ?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                notificationPresenter.updateNotificationSetting(false, "", "");


            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onLogout(String message) {

        MyToast.showMessage(getActivity(), message);
        dataManager.getPreferencesHelper().storeToken(null);
        dataManager.getPreferencesHelper().storeUserId(-1);
        dataManager.getPreferencesHelper().setImagePath(null);
        dataManager.getPreferencesHelper().storeUserName(null);
        dataManager.getPreferencesHelper().storeUserRole(null);
        getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();

    }

    @Override
    public void onLogoutError(Error error) {

        UtilSnackbar.showSnakbarError(parent, error.getMessage());
    }

    @Override
    public void onTokenExpire(Error error) {
        //   MyToast.showMessage(getActivity(),error.getMessage());
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
        activity.replaceFragment(new ChangePassword(), "Change Password");

    }
}
