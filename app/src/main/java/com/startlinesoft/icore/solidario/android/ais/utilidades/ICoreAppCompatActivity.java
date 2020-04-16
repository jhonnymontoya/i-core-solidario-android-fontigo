package com.startlinesoft.icore.solidario.android.ais.utilidades;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.startlinesoft.icore.solidario.android.ais.R;

public class ICoreAppCompatActivity extends AppCompatActivity {

    private static final String TOKEN = "TOKEN";

    /**
     * Retorna true o false si en el almacen interno se enuentra un token
     * de autenticación
     * @return
     */
    protected boolean isSetToken() {
        SharedPreferences preferencias = this.getAlmacenDePreferencias();
        return preferencias.contains(ICoreAppCompatActivity.TOKEN);
    }

    /**
     * Retorna el token de autenticación almacenado en el almacen
     * @return
     */
    protected String getToken() {
        SharedPreferences preferencias = this.getAlmacenDePreferencias();
        String token = preferencias.getString(ICoreAppCompatActivity.TOKEN, null);
        return token;
    }

    /**
     * Guarda un token de autenticación en el almacen
     * @param token
     */
    protected void putToken(String token) {
        SharedPreferences preferencias = this.getAlmacenDePreferencias();
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString(ICoreAppCompatActivity.TOKEN, token);
        editor.commit();
    }

    /**
     * elimina un token de autenticación
     */
    protected void removeToken() {
        SharedPreferences preferencias = this.getAlmacenDePreferencias();
        SharedPreferences.Editor editor = preferencias.edit();
        editor.remove(ICoreAppCompatActivity.TOKEN);
        editor.commit();
    }

    protected void verificarRed() {
        if(this.isOnline() == false) {
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            msg.setTitle(this.getString(R.string.app_name));
            msg.setMessage("La conexión a internet es requerida, por favor verifique su conexión a internet");
            msg.setCancelable(false);
            msg.setPositiveButton("Reintentar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if(isOnline() == false) {
                                msg.create().show();
                            }
                        }
                    });
            msg.create().show();
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network n = cm.getActiveNetwork();
            if(n == null) {
                return false;
            }
            NetworkCapabilities nc =  cm.getNetworkCapabilities(n);
            if(nc == null) {
                return false;
            }
            if(nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true;
            }
            if(nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true;
            }
            return false;
        }
        else {
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if(ni == null) {
                return false;
            }
            return ni.isConnected();
        }
    }

    private SharedPreferences getAlmacenDePreferencias() {
        String nombreRecurso = this.getString(R.string.token_info);
        return this.getSharedPreferences(nombreRecurso, Context.MODE_PRIVATE);
    }
}
