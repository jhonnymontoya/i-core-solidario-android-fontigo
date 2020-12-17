package com.startlinesoft.icore.solidario.android.ais;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreConstantes;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

public class ConfiguracionFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener, DialogInterface.OnClickListener {

    private Context context;
    private AlertDialog.Builder alert;
    private Preference touchIdPreference;

    private final int REQUESTCODE_ENROLL = 1;
    private final int REQUESTCODE_ACTIVARTOUCHID = 2;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        this.setPreferencesFromResource(R.xml.configuraciones, rootKey);

        this.touchIdPreference = this.findPreference(ICoreConstantes.PREFERENCE_TOUCHID);
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
        if (preference.equals(this.touchIdPreference)) {
            if ((boolean) newValue) {
                //Si el biometrico no se encuentra enrolado
                //se pregunta si se desea enrolar
                if (!this.biometricoEnrolado()) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) { //OREO 8, 8.1
                        this.alert.setMessage(R.string.configuracion_alert_touchid_title_d)
                                .setCancelable(true)
                                .setPositiveButton(R.string.ok, null)
                                .show();
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) { //PIE 9, Android 10
                        this.alert.setMessage(R.string.configuracion_alert_touchid_title)
                                .setCancelable(true)
                                .setPositiveButton(R.string.configuracion_alert_touchid_yes, this)
                                .setNegativeButton(R.string.configuracion_alert_touchid_no, this)
                                .show();
                    }
                    return false;
                }

                //Se llama a actividad para activar TouchID
                Intent i = new Intent(this.context, ActivarTouchIDActivity.class);
                this.startActivityForResult(i, this.REQUESTCODE_ACTIVARTOUCHID);
            } else {
                desactivarTouch();
            }
            return true;
        }
        return true;
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

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P) { //PiE 9
                final Intent enroll = new Intent(Settings.ACTION_FINGERPRINT_ENROLL);
                startActivityForResult(enroll, this.REQUESTCODE_ENROLL);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { //Android 10, ...
                final Intent enroll = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enroll.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                startActivityForResult(enroll, this.REQUESTCODE_ENROLL);
            }
            return;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Resultado por activar touchID
        if (requestCode == this.REQUESTCODE_ACTIVARTOUCHID) {
            if (resultCode == ICoreConstantes.RESULT_OK) {
                SwitchPreference sp = (SwitchPreference) this.touchIdPreference;
                sp.setChecked(true);
            } else if (resultCode == ICoreConstantes.RESULT_CANCEL) {
                SwitchPreference sp = (SwitchPreference) this.touchIdPreference;
                sp.setChecked(false);
            }
        }
    }

    private SharedPreferences getAlmacenPreferencias() {
        return PreferenceManager.getDefaultSharedPreferences(this.context);
    }

    private void desactivarTouch() {
        SharedPreferences sp = this.getAlmacenPreferencias();
        SharedPreferences.Editor editor = sp.edit();

        editor.remove(ICoreConstantes.LOGIN_PASSWORD);
        editor.remove(ICoreConstantes.LOGIN_PASSWORD);

        editor.putBoolean(ICoreConstantes.PREFERENCE_TOUCHID, false);

        editor.apply();

    }
}
