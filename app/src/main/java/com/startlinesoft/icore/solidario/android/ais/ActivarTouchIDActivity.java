package com.startlinesoft.icore.solidario.android.ais;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.startlinesoft.icore.solidario.ApiClient;
import com.startlinesoft.icore.solidario.ApiException;
import com.startlinesoft.icore.solidario.android.ais.databinding.ActivityActivartouchidBinding;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreApiClient;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreConstantes;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreGeneral;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreKeyStore;
import com.startlinesoft.icore.solidario.api.LoginApi;
import com.startlinesoft.icore.solidario.api.models.LoginInfo;
import com.startlinesoft.icore.solidario.api.models.LoginToken;
import com.startlinesoft.icore.solidario.api.models.Socio;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;

public class ActivarTouchIDActivity extends ICoreAppCompatActivity implements View.OnClickListener {

    private ActivityActivartouchidBinding bnd;

    private Socio socio;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bnd = ActivityActivartouchidBinding.inflate(this.getLayoutInflater());
        this.setContentView(this.bnd.getRoot());

        //Se valida token activo
        this.validarLogin();

        //Se genera una nueva llave secreta
        ICoreKeyStore.generarLlaveSecreta();

        this.bnd.btnLogin.setOnClickListener(this);
        this.bnd.tvCancelar.setOnClickListener(this);

        this.socio = ICoreGeneral.getSocio();
        String mensaje = this.getString(R.string.activartouch_mensaje);
        mensaje = String.format(mensaje, this.socio.getPrimerNombre());
        this.bnd.tvNombre.setText(mensaje);

        this.bnd.etPassword.addTextChangedListener(this.getTextWatcher());

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
                int password = bnd.etPassword.getText().toString().length();
                bnd.btnLogin.setEnabled(password > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        //Activar
        if(v.equals(this.bnd.btnLogin)){
            this.bnd.progressBar.setVisibility(View.VISIBLE);

            this.verificarRed();

            ApiClient cliente = ICoreApiClient.getApiClient();
            LoginApi loginApi = new LoginApi(cliente);

            String usuario = this.getUsuarioDeUsuarioLogin();
            this.password = this.bnd.etPassword.getText().toString();

            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setUsuario(usuario);
            loginInfo.setPassword(password);

            new Thread(() -> {
                try {
                    loginApi.validarCredenciales(loginInfo);
                    this.bnd.progressBar.post(() -> {
                        this.bnd.progressBar.setVisibility(View.GONE);
                        bnd.tvError.setVisibility(View.GONE);
                        this.activarBiometrico();
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
                    });
                }
            }).start();
        }

        //Cancelar
        if(v.equals(this.bnd.tvCancelar)){
            this.setResult(ICoreConstantes.RESULT_CANCEL);
            this.finish();
        }
    }

    private BiometricPrompt.AuthenticationCallback getBiometricCallbackEncriptar(String mensaje) {
        return new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                byte[] bytesPassword = mensaje.getBytes(Charset.defaultCharset());
                byte[] encryptedInfo = new byte[0];
                try {
                    Cipher cipher = result.getCryptoObject().getCipher();
                    encryptedInfo = cipher.doFinal(bytesPassword);
                    guardarPasswordEncriptadoDeUsuarioLogin(encryptedInfo, cipher.getIV());
                    finalizar();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        };
    }

    private void finalizar(){
        this.setResult(ICoreConstantes.RESULT_OK);
        this.finish();
    }

    private void activarBiometrico(){
        this.executor = ContextCompat.getMainExecutor(this);

        String titulo = this.getString(R.string.touchid_titulo);
        String cancelar = this.getString(R.string.activartouch_cancelar);

        this.promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(titulo)
                .setSubtitle(this.socio.getNombre())
                .setNegativeButtonText(cancelar)
                .build();

        this.biometricPrompt = new BiometricPrompt(
                this,
                this.executor,
                this.getBiometricCallbackEncriptar(this.password));

        Cipher cipher = ICoreKeyStore.getCipher();
        SecretKey secretKey = ICoreKeyStore.getLlaveSecreta();
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            this.biometricPrompt.authenticate(
                    this.promptInfo,
                    new BiometricPrompt.CryptoObject(cipher)
            );
        } catch (InvalidKeyException e) {
        }
    }
}