package com.startlinesoft.icore.solidario.android.fontigo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.startlinesoft.icore.solidario.ApiClient;
import com.startlinesoft.icore.solidario.ApiException;
import com.startlinesoft.icore.solidario.android.fontigo.databinding.ActivityLoginBinding;
import com.startlinesoft.icore.solidario.android.fontigo.utilidades.ICoreApiClient;
import com.startlinesoft.icore.solidario.android.fontigo.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.android.fontigo.utilidades.ICoreKeyStore;
import com.startlinesoft.icore.solidario.api.LoginApi;
import com.startlinesoft.icore.solidario.api.models.LoginInfo;
import com.startlinesoft.icore.solidario.api.models.LoginToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.concurrent.Executor;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class LoginActivity extends ICoreAppCompatActivity implements View.OnClickListener {

    private ActivityLoginBinding bnd;

    private boolean usuarioVisible = true;
    private boolean passwordVisible = true;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bnd = ActivityLoginBinding.inflate(this.getLayoutInflater());
        this.setContentView(bnd.getRoot());

        this.bnd.etUser.addTextChangedListener(this.getTextWatcher());
        this.bnd.etPassword.addTextChangedListener(this.getTextWatcher());
        this.bnd.btnLogin.setOnClickListener(this);
        this.bnd.tvNoUsuario.setOnClickListener(this);
        this.bnd.tvForgotPassword.setOnClickListener(this);
        this.bnd.ivFingerprint.setOnClickListener(this);

        this.prepararLogin();
    }

    private void prepararLogin() {
        if (!this.existenDatosDeUsuarioLogin()) {
            return;
        }

        String nombre = this.getNombreDeUsuarioLogin();
        String mensajeNombre = String.format(
                "%s %s",
                this.getString(R.string.login_hola),
                nombre
        );
        String mensajeNoUsuario = String.format(
                "%s %s",
                this.getString(R.string.login_nousuario),
                nombre
        );

        //Caso cuando ya existe usuario pero no touchId
        this.usuarioVisible = false;
        this.bnd.etUser.setVisibility(View.GONE);
        this.bnd.tvNombre.setText(mensajeNombre);
        this.bnd.tvNombre.setVisibility(View.VISIBLE);
        this.bnd.tvNoUsuario.setText(mensajeNoUsuario);
        this.bnd.tvNoUsuario.setVisibility(View.VISIBLE);

        //Caso cuando el logueo es por TouchID
        if (this.getTouchIdActivo()) {
            this.passwordVisible = false;
            this.bnd.etPassword.setVisibility(View.GONE);
        }

        //Imagen del asociado
        if (this.existeAvatarDeUsuarioLogin()) {
            this.bnd.ivAvatarContainer.setVisibility(View.VISIBLE);
            this.bnd.ivImagen.setImageBitmap(this.getAvatarDeUsuarioLogin());
            this.bnd.ivLogo.setVisibility(View.GONE);
        }

        this.actualizarEstadoBotonSubmit();
    }

    private TextWatcher getTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                actualizarEstadoBotonSubmit();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    private void actualizarEstadoBotonSubmit() {
        if (this.usuarioVisible && this.passwordVisible) {
            int usuario = this.bnd.etUser.getText().toString().length();
            int password = this.bnd.etPassword.getText().toString().length();
            this.bnd.btnLogin.setEnabled(usuario > 0 && password > 0);
        } else if (this.passwordVisible) {
            int password = this.bnd.etPassword.getText().toString().length();
            this.bnd.btnLogin.setEnabled(password > 0);
        } else {
            this.bnd.btnLogin.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v.equals(this.bnd.btnLogin)) {
            this.verificarRed();

            if (this.getTouchIdActivo() && !this.passwordVisible) {
                this.mostrarBiometrico();
            } else {
                String password = this.bnd.etPassword.getText().toString();
                this.performLogin(password);
            }
        }

        if (v.equals(this.bnd.tvNoUsuario)) {
            this.bnd.tvError.setVisibility(View.GONE);
            this.noUsuario();
        }

        if (v.equals(this.bnd.tvForgotPassword)) {
            this.bnd.tvError.setVisibility(View.GONE);
            String usuario = this.bnd.etUser.getText().toString().trim();
            Intent i = new Intent(getApplicationContext(), OlvideClaveActivity.class);
            i.putExtra("USUARIO", usuario);
            startActivity(i);
        }

        if (v.equals(this.bnd.ivFingerprint)) {
            this.mostrarBiometrico();
        }
    }

    private void performLogin(String password) {
        ApiClient cliente = ICoreApiClient.getApiClient();
        LoginApi loginApi = new LoginApi(cliente);

        String usuario = this.usuarioVisible ?
                this.bnd.etUser.getText().toString() : this.getUsuarioDeUsuarioLogin();

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsuario(usuario);
        loginInfo.setPassword(password);

        bnd.progressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            try {
                LoginToken loginToken = loginApi.login(loginInfo);
                putToken(loginToken.getToken());
                ICoreApiClient.setToken(loginToken.getToken());

                Intent i = new Intent(getBaseContext(), DashBoardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
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
                    this.bnd.etPassword.setText("");
                });
            }
        }).start();
    }

    private void mostrarBiometrico() {
        this.executor = ContextCompat.getMainExecutor(this);

        String titulo = this.getString(R.string.touchid_titulo);
        String cancelar = this.getString(R.string.activartouch_cancelar);

        this.promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(titulo)
                .setSubtitle(this.getNombreCortoDeUsuarioLogin())
                .setNegativeButtonText(cancelar)
                .build();

        Cipher cipher = ICoreKeyStore.getCipher();
        SecretKey secretKey = ICoreKeyStore.getLlaveSecreta();

        byte[] mensaje = this.getPassword();
        this.biometricPrompt = new BiometricPrompt(
                this,
                this.executor,
                this.getBiometricCallbackDesencriptar(mensaje)
        );
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(this.getVectorIV());
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            this.biometricPrompt.authenticate(
                    this.promptInfo,
                    new BiometricPrompt.CryptoObject(cipher)
            );
        } catch (InvalidKeyException e) {
        } catch (InvalidAlgorithmParameterException e) {
        }
    }

    private BiometricPrompt.AuthenticationCallback getBiometricCallbackDesencriptar(byte[] password) {
        return new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                mostrarCampoPasswordBotonBiometrico();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                try {
                    String decripted = new String(result.getCryptoObject().getCipher().doFinal(password));
                    performLogin(decripted);
                } catch (BadPaddingException e) {
                    //desencriptado.setText("Mensaje existe pero llave invalidada, mensaje perdido para siempre");
                } catch (IllegalBlockSizeException e) {
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        };
    }

    private void mostrarCampoPasswordBotonBiometrico() {
        this.bnd.etPassword.setVisibility(View.VISIBLE);
        this.bnd.ivFingerprint.setVisibility(View.VISIBLE);
        this.passwordVisible = true;
    }

    private void noUsuario() {
        this.limpiarDatosDeUsuarioLogin();
        this.limpiarDatosDeTouchId();

        this.usuarioVisible = true;
        this.passwordVisible = true;

        this.bnd.etUser.setVisibility(View.VISIBLE);
        this.bnd.etPassword.setVisibility(View.VISIBLE);
        this.bnd.tvNombre.setVisibility(View.GONE);
        this.bnd.tvNoUsuario.setVisibility(View.GONE);

        this.bnd.ivAvatarContainer.setVisibility(View.GONE);
        this.bnd.ivLogo.setVisibility(View.VISIBLE);

        this.actualizarEstadoBotonSubmit();
    }
}