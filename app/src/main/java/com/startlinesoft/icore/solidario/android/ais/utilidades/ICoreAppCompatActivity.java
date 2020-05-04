package com.startlinesoft.icore.solidario.android.ais.utilidades;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.startlinesoft.icore.solidario.ApiClient;
import com.startlinesoft.icore.solidario.ApiException;
import com.startlinesoft.icore.solidario.android.ais.LoginActivity;
import com.startlinesoft.icore.solidario.android.ais.R;
import com.startlinesoft.icore.solidario.api.LoginApi;

public class ICoreAppCompatActivity extends AppCompatActivity {

    private static final String TOKEN = "TOKEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    /**
     * Remueve el título de la barra ToolBar
     */
    protected void removerTituloBarra() {
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    /**
     * Sale del sistema
     */
    protected void logout() {
        this.verificarRed();

        ApiClient cliente = ICoreApiClient.getApiClient();
        LoginApi loginApi = new LoginApi(cliente);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    loginApi.logout();
                } catch (ApiException e) {}
                Intent i = new Intent(getBaseContext(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        }).start();
    }

    /**
     * Valida que se encuentre logueado, de lo contrario lo envia al login
     */
    protected void validarLogin() {
        this.verificarRed();
        if(this.isSetToken()) {
            ICoreApiClient.setToken(this.getToken());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean esTokenValido =  ICoreApiClient.esTokenValido();
                    if(esTokenValido == false) {
                        //No es válido e ir al login
                        Intent i = new Intent(getBaseContext(), LoginActivity.class);
                        removeToken();
                        startActivity(i);
                        finish();
                    }
                }
            }).start();
        }
        else {
            Intent i = new Intent(getBaseContext(), LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            this.removeToken();
            startActivity(i);
            finish();
        }
    }

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
