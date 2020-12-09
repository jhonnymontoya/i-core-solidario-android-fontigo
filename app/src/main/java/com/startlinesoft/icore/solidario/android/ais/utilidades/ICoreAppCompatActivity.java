package com.startlinesoft.icore.solidario.android.ais.utilidades;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Base64;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.startlinesoft.icore.solidario.ApiClient;
import com.startlinesoft.icore.solidario.ApiException;
import com.startlinesoft.icore.solidario.android.ais.LoginActivity;
import com.startlinesoft.icore.solidario.android.ais.R;
import com.startlinesoft.icore.solidario.api.LoginApi;
import com.startlinesoft.icore.solidario.api.models.Socio;

public class ICoreAppCompatActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Valida que se encuentre logueado, de lo contrario lo envia al login screen
     */
    protected void validarLogin() {
        this.verificarRed();
        if (this.isSetToken()) {
            if (!ICoreApiClient.esTokenValido()) {
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

        Thread t = new Thread(() -> {
            try {
                loginApi.logout();
            } catch (ApiException ignored) {
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException ignored) {
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
        if (this.isNotOnline()) {
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            msg.setTitle(this.getString(R.string.app_name));
            msg.setMessage(this.getString(R.string.network_error));
            msg.setCancelable(false);
            msg.setPositiveButton(this.getString(R.string.network_retry), (dialog, which) -> {
                if (isNotOnline()) {
                    msg.create().show();
                }
            });
            msg.create().show();
        }
    }

    private boolean isNotOnline() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network n = cm.getActiveNetwork();
        if (n == null) {
            return true;
        }

        NetworkCapabilities nc = cm.getNetworkCapabilities(n);
        if (nc == null) {
            return true;
        }
        if (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return false;
        }
        return !nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
    }

    protected boolean isSetToken() {
        SharedPreferences sp = this.getAlmacenPreferencias();
        return sp.contains(ICoreConstantes.TOKEN);
    }

    protected String getToken() {
        SharedPreferences sp = this.getAlmacenPreferencias();
        return sp.getString(ICoreConstantes.TOKEN, null);
    }

    protected void putToken(String token) {
        SharedPreferences sp = this.getAlmacenPreferencias();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(ICoreConstantes.TOKEN, token);
        editor.apply();
    }

    protected void removerToken() {
        SharedPreferences sp = this.getAlmacenPreferencias();
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(ICoreConstantes.TOKEN);
        editor.apply();
    }

    protected void vibrar() {
        Vibrator vibrator = this.getVibrador();
        if (this.hasVibrador()) {
            VibrationEffect effect;
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
        return vibrator != null;
    }

    private Vibrator getVibrador() {
        return (Vibrator) this.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
    }

    private SharedPreferences getAlmacenPreferencias() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }

    protected boolean isVibradorActivado() {
        if (!this.hasVibrador()) {
            return false;
        }
        SharedPreferences sp = this.getAlmacenPreferencias();
        return sp.getBoolean(ICoreConstantes.PREFERENCE_VIBRADOR, true);
    }

    /**
     * En el almacen de preferencias, inicializa con llaves predefinidas
     */
    protected void inicializarPreferencias() {
        SharedPreferences sp = this.getAlmacenPreferencias();
        SharedPreferences.Editor editor = null;
        if (!sp.contains(ICoreConstantes.PREFERENCE_VIBRADOR)) {
            editor = sp.edit();
            editor.putBoolean(ICoreConstantes.PREFERENCE_VIBRADOR, true);
        }
        if (!sp.contains(ICoreConstantes.PREFERENCE_TOUCHID)) {
            editor = sp.edit();
            editor.putBoolean(ICoreConstantes.PREFERENCE_TOUCHID, false);
        }
        if (editor != null) {
            editor.apply();
        }
    }

    @Override
    public void onClick(View v) {
        if (this.isVibradorActivado()) {
            this.vibrar();
        }
    }

    /**
     * Almacena los datos del usuario en el almacen interno
     */
    protected void guardarDatosDeUsuarioLogin() {
        Socio socio = ICoreGeneral.getSocio();
        if (socio == null) {
            return;
        }

        //Se limpian datos previos
        this.limpiarDatosDeUsuarioLogin();

        SharedPreferences sp = this.getAlmacenPreferencias();
        SharedPreferences.Editor editor = sp.edit();

        String usuario = socio.getIdentificacion().replaceAll("[,\\.]", "");
        editor.putString(ICoreConstantes.LOGIN_USUARIO, usuario);

        editor.putString(ICoreConstantes.LOGIN_NOMBRE, socio.getPrimerNombre());

        if (socio.getEsImagenReal()) {
            String imagen = Base64.encodeToString(socio.getImagen(), Base64.DEFAULT);
            editor.putString(ICoreConstantes.LOGIN_AVATAR, imagen);
        }

        editor.apply();

    }

    protected boolean existenDatosDeUsuarioLogin() {
        SharedPreferences sp = this.getAlmacenPreferencias();
        return sp.contains(ICoreConstantes.LOGIN_USUARIO) && sp.contains(ICoreConstantes.LOGIN_NOMBRE);
    }

    protected boolean existeAvatarDeUsuarioLogin() {
        SharedPreferences sp = this.getAlmacenPreferencias();
        return sp.contains(ICoreConstantes.LOGIN_AVATAR);
    }

    protected byte[] getAvatarDeUsuarioLogin() {
        SharedPreferences sp = this.getAlmacenPreferencias();
        return Base64.decode(sp.getString(ICoreConstantes.LOGIN_AVATAR, null), Base64.DEFAULT);
    }

    protected String getUsuarioDeUsuarioLogin() {
        SharedPreferences sp = this.getAlmacenPreferencias();
        return sp.getString(ICoreConstantes.LOGIN_USUARIO, null);
    }

    protected String getNombreDeUsuarioLogin() {
        SharedPreferences sp = this.getAlmacenPreferencias();
        return sp.getString(ICoreConstantes.LOGIN_NOMBRE, null);
    }

    /**
     * Limpia del almacen de preferencias datos de logueo del usuario
     */
    protected void limpiarDatosDeUsuarioLogin() {
        SharedPreferences sp = this.getAlmacenPreferencias();
        SharedPreferences.Editor editor = sp.edit();
        boolean borrar = false;

        if (sp.contains(ICoreConstantes.LOGIN_USUARIO)) {
            editor.remove(ICoreConstantes.LOGIN_USUARIO);
            borrar = true;
        }

        if (sp.contains(ICoreConstantes.LOGIN_NOMBRE)) {
            editor.remove(ICoreConstantes.LOGIN_NOMBRE);
            borrar = true;
        }

        if (sp.contains(ICoreConstantes.LOGIN_AVATAR)) {
            editor.remove(ICoreConstantes.LOGIN_AVATAR);
            borrar = true;
        }

        if (borrar) {
            editor.apply();
        }
    }
}
