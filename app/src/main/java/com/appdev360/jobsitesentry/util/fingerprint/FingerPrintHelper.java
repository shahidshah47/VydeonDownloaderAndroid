package com.appdev360.jobsitesentry.util.fingerprint;

import android.app.KeyguardManager;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import androidx.annotation.RequiresApi;

import com.appdev360.jobsitesentry.R;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.SecretKey;

import static android.content.Context.FINGERPRINT_SERVICE;
import static android.content.Context.KEYGUARD_SERVICE;

/**
 * Created by abubaker on 3/12/18.
 * <p>
 * Updated By Hussain Saad on 27/01/22
 */

public class FingerPrintHelper {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";


    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean checkFingerPrint(Context context) {

        // Keyguard Manager
        KeyguardManager keyguardManager = (KeyguardManager)
                context.getSystemService(KEYGUARD_SERVICE);

        // Fingerprint Manager
        FingerprintManager fingerprintManager = (FingerprintManager)
                context.getSystemService(FINGERPRINT_SERVICE);

        try {
            // Check if the fingerprint sensor is present
            if (!fingerprintManager.isHardwareDetected()) {
                // Update the UI with a eventType
                return false;
            }

            if (!fingerprintManager.hasEnrolledFingerprints()) {
              //  MyToast.showMessage(context,context.getResources().getString(R.string.no_fingerprint_configured));
                return false;
            }

            if (!keyguardManager.isKeyguardSecure()) {
              //  MyToast.showMessage(context,context.getResources().getString(R.string.secure_lock_not_enabled));
                return false;
            }
        }
        catch(SecurityException se) {
            se.printStackTrace();
        }
        return true;
    }

/*    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String decryptMsg(byte[] cipherText, String alias)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        try {
            keyStore.load(null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        KeyStore.SecretKeyEntry secretKeyEntry = null;
        try {
            secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore
                    .getEntry(alias, null);
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        final SecretKey secretKey = secretKeyEntry.getSecretKey();
        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        final GCMParameterSpec spec;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            spec = new GCMParameterSpec(128, encryptionIv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
        }



    }*/


/*    *//* Decrypt the eventType, given derived encContentValues and initialization vector. *//*
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        String decryptString = new String(cipher.doFinal(cipherText), "UTF-8");
        return decryptString;
    }*/


    public static SecretKey getSecretKey(Context context) throws KeyStoreException {

        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        try {
            keyStore.load(null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        KeyStore.SecretKeyEntry secretKeyEntry = null;
        try {
            secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore
                    .getEntry(context.getResources().getString(R.string.jobsitesentry), null);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        }

        final SecretKey secretKey = secretKeyEntry.getSecretKey();
        return secretKey;
    }


}
