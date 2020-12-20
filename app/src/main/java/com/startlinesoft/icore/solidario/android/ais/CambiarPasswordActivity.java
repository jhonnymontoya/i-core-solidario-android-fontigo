package com.startlinesoft.icore.solidario.android.ais;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.startlinesoft.icore.solidario.ApiClient;
import com.startlinesoft.icore.solidario.ApiException;
import com.startlinesoft.icore.solidario.android.ais.databinding.ActivityCambiarpasswordBinding;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreApiClient;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreConstantes;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreGeneral;
import com.startlinesoft.icore.solidario.api.LoginApi;
import com.startlinesoft.icore.solidario.api.models.CambioPasswordObject;
import com.startlinesoft.icore.solidario.api.models.LoginInfo;
import com.startlinesoft.icore.solidario.api.models.Socio;

import org.json.JSONException;
import org.json.JSONObject;

public class CambiarPasswordActivity extends ICoreAppCompatActivity implements View.OnClickListener {

    private ActivityCambiarpasswordBinding bnd;
    private Socio socio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bnd = ActivityCambiarpasswordBinding.inflate(this.getLayoutInflater());
        this.setContentView(this.bnd.getRoot());

        //Se valida token activo
        this.validarLogin();

        this.bnd.btnCambiarPassword.setOnClickListener(this);
        this.bnd.tvCancelar.setOnClickListener(this);

        this.socio = ICoreGeneral.getSocio();
        String mensaje = this.getString(R.string.cambiarpassword_mensaje);
        mensaje = String.format(mensaje, this.socio.getPrimerNombre());
        this.bnd.tvNombre.setText(mensaje);

        this.bnd.etPassword.addTextChangedListener(this.getTextWatcher());
        this.bnd.etNewPassword.addTextChangedListener(this.getTextWatcher());
        this.bnd.etConfirmPassword.addTextChangedListener(this.getTextWatcher());

        //Imagen del asociado
        if(this.existeAvatarDeUsuarioLogin()) {
            this.bnd.ivAvatarContainer.setVisibility(View.VISIBLE);
            this.bnd.ivImagen.setImageBitmap(this.getAvatarDeUsuarioLogin());
            this.bnd.ivLogo.setVisibility(View.GONE);
        }
    }

    private TextWatcher getTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int p = bnd.etPassword.getText().toString().length();
                int np = bnd.etNewPassword.getText().toString().length();
                int cp = bnd.etConfirmPassword.getText().toString().length();
                bnd.btnCambiarPassword.setEnabled(p > 0 && np > 0 && cp > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        //Cambiar contraseña
        if(v.equals(this.bnd.btnCambiarPassword)){
            this.bnd.progressBar.setVisibility(View.VISIBLE);

            this.verificarRed();

            ApiClient cliente = ICoreApiClient.getApiClient();
            LoginApi loginApi = new LoginApi(cliente);

            String usuario = this.getUsuarioDeUsuarioLogin();

            CambioPasswordObject cambioPasswordObject = new CambioPasswordObject();
            cambioPasswordObject.setUsuario(usuario);
            cambioPasswordObject.setPasswordActual(this.bnd.etPassword.getText().toString());
            cambioPasswordObject.setPassword(this.bnd.etNewPassword.getText().toString());
            cambioPasswordObject.setConfirmarPassword(this.bnd.etConfirmPassword.getText().toString());

            new Thread(() -> {
                try {
                    loginApi.cambiarPassword(cambioPasswordObject);
                    this.bnd.progressBar.post(() -> {
                        this.bnd.progressBar.setVisibility(View.GONE);
                        bnd.tvError.setVisibility(View.GONE);
                        this.limpiarDatosDeTouchId();
                        this.finish();
                    });
                } catch (ApiException e) {
                    bnd.tvError.post(() -> {
                        String msg;
                        switch (e.getCode()) {
                            case 400: //Entradas no válidas
                            case 422: //Entradas no procesables
                            case 429: //Demasiados intentos
                                msg = "Datos no válidos";
                                break;
                            case 426: //Se requiere actualización
                                msg = "Se requiere actualización";
                                break;
                            case 401:
                                msg = "Usuario o contraseña no válidos";
                                break;
                            case 412:
                                try {
                                    msg = new JSONObject(e.getResponseBody()).getString("message");
                                } catch (JSONException je) {
                                    msg = "App Movil no activa";
                                }
                                break;
                            default:
                                msg = "Entradas no válidos";
                                break;
                        }
                        bnd.tvError.setText(msg);
                        bnd.tvError.setVisibility(View.VISIBLE);
                        bnd.progressBar.setVisibility(View.GONE);
                        bnd.etPassword.setText("");
                        bnd.etNewPassword.setText("");
                        bnd.etConfirmPassword.setText("");
                    });
                }
            }).start();
        }

        //Cancelar
        if(v.equals(this.bnd.tvCancelar)){
            this.finish();
        }

    }

}