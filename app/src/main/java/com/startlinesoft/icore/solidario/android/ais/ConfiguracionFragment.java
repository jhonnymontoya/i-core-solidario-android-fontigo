package com.startlinesoft.icore.solidario.android.ais;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreConstantes;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

public class ConfiguracionFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener, DialogInterface.OnClickListener {

    private Context context;
    private AlertDialog.Builder alert;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        this.setPreferencesFromResource(R.xml.configuraciones, rootKey);

        Preference touchIdPreference = this.findPreference(ICoreConstantes.PREFERENCE_TOUCHID);
        touchIdPreference.setOnPreferenceChangeListener(this);

        if (!this.existeBiometrico()) {
            touchIdPreference.setEnabled(false);
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;

        this.alert = new AlertDialog.Builder(this.context);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        //Si el biometrico no se encuentra enrolado
        //se pregunta si se desea enrolar
        if (!this.biometricoEnrolado()) {
            this.alert.setMessage(R.string.configuracion_alert_touchid_title)
                    .setCancelable(true)
                    .setPositiveButton(R.string.configuracion_alert_touchid_yes, this)
                    .setNegativeButton(R.string.configuracion_alert_touchid_no, this)
                    .show();
            return false;
        }
        Toast.makeText(this.getContext(), newValue.toString(), Toast.LENGTH_SHORT).show();
        return false;
    }

    private boolean existeBiometrico() {
        BiometricManager biometricManager = BiometricManager.from(this.context);
        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED: //Si se puede pero no hay huellas activas
            case BiometricManager.BIOMETRIC_SUCCESS:
            case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                return true;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
            default:
                return false;
        }
    }

    private boolean biometricoEnrolado() {
        BiometricManager biometricManager = BiometricManager.from(this.context);
        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                return false;
            default:
                return true;
        }
    }

    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        // Generate a 256-bit key
        final int outputKeyLength = 256;

        SecureRandom secureRandom = new SecureRandom();
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(outputKeyLength, secureRandom);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            final Intent enroll = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
            enroll.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
            startActivityForResult(enroll, 1);
            return;
        }
    }
}
