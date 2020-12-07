package com.startlinesoft.icore.solidario.android.ais.utilidades;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.startlinesoft.icore.solidario.ApiClient;
import com.startlinesoft.icore.solidario.ApiException;
import com.startlinesoft.icore.solidario.android.ais.LoginActivity;
import com.startlinesoft.icore.solidario.android.ais.R;
import com.startlinesoft.icore.solidario.api.LoginApi;

public class ICoreAppCompatActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    /**
     * Valida que se encuentre logueado, de lo contrario lo envia al login screen
     */
    protected void validarLogin() {
        this.verificarRed();
        if (this.isSetToken()) {
            boolean esTokenValido = ICoreApiClient.esTokenValido();
            if (esTokenValido == false) {
                //Token existe pero no es válido o ya venció
                Intent i = new Intent(this.getBaseContext(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                this.removerToken();
                this.startActivity(i);
                this.finish();
            }
        } else {
            Intent i = new Intent(this.getBaseContext(), LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            this.removerToken();
            this.startActivity(i);
            this.finish();
        }
    }

    /**
     * Sale del sistema.
     */
    protected void logout() {
        this.verificarRed();

        ApiClient cliente = ICoreApiClient.getApiClient();
        LoginApi loginApi = new LoginApi(cliente);
        try {
            loginApi.logout();
        } catch (ApiException e) {
        }
        this.removerToken();
        Intent i = new Intent(getBaseContext(), LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    /**
     * Remueve el título de la barra ToolBar
     */
    protected void removerTituloBarra() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    protected void verificarRed() {
        if (this.isOnline() == false) {
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            msg.setTitle(this.getString(R.string.app_name));
            msg.setMessage(this.getString(R.string.network_error));
            msg.setCancelable(false);
            msg.setPositiveButton(this.getString(R.string.network_retry), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (isOnline() == false) {
                        msg.create().show();
                    }
                }
            });
            msg.create().show();
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network n = cm.getActiveNetwork();
        if (n == null) {
            return false;
        }

        NetworkCapabilities nc = cm.getNetworkCapabilities(n);
        if (nc == null) {
            return false;
        }
        if (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return true;
        }
        if (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            return true;
        }
        return false;
    }

    private SharedPreferences getAlmacenDePreferencias() {
        String nombreRecurso = this.getString(R.string.app_preferences);
        return this.getSharedPreferences(nombreRecurso, Context.MODE_PRIVATE);
    }

    protected boolean isSetToken() {
        SharedPreferences sp = this.getAlmacenDePreferencias();
        return sp.contains(ICoreConstantes.TOKEN);
    }

    protected String getToken() {
        SharedPreferences sp = this.getAlmacenDePreferencias();
        String token = sp.getString(ICoreConstantes.TOKEN, null);
        return token;
    }

    protected void putToken(String token) {
        SharedPreferences sp = this.getAlmacenDePreferencias();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(ICoreConstantes.TOKEN, token);
        editor.commit();
    }

    protected void removerToken() {
        SharedPreferences sp = this.getAlmacenDePreferencias();
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(ICoreConstantes.TOKEN);
        editor.commit();
    }

    protected void vibrar() {
        Vibrator vibrator = this.getVibrador();
        if (this.hasVibrador() == true) {
            VibrationEffect effect = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                effect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK);
            } else {
                effect = VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE);
            }
            vibrator.vibrate(effect);
        }
    }

    private boolean hasVibrador() {
        Vibrator vibrator = this.getVibrador();
        return vibrator != null ? true : false;
    }

    private Vibrator getVibrador() {
        Vibrator v = (Vibrator) this.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        return v;
    }

    private SharedPreferences getSharedPreferences() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        return sp;
    }

    private boolean isVibradorActivado() {
        if(this.hasVibrador() == false){
            return false;
        }
        SharedPreferences sp = this.getAlmacenDePreferencias();
        boolean res = sp.getBoolean(ICoreConstantes.PREFERENCE_VIBRADOR, true);
        return res;
    }

    @Override
    public void onClick(View v) {
        this.vibrar();
    }
}
