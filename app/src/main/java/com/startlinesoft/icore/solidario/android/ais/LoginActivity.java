package com.startlinesoft.icore.solidario.android.ais;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.startlinesoft.icore.solidario.ApiClient;
import com.startlinesoft.icore.solidario.ApiException;
import com.startlinesoft.icore.solidario.android.ais.databinding.ActivityLoginBinding;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreApiClient;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.api.LoginApi;
import com.startlinesoft.icore.solidario.api.models.LoginInfo;
import com.startlinesoft.icore.solidario.api.models.LoginToken;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends ICoreAppCompatActivity implements View.OnClickListener {

    private ActivityLoginBinding bnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bnd = ActivityLoginBinding.inflate(this.getLayoutInflater());
        this.setContentView(bnd.getRoot());

        this.bnd.etUser.addTextChangedListener(this.getTextWatcher());
        this.bnd.etPassword.addTextChangedListener(this.getTextWatcher());
        this.bnd.btnLogin.setOnClickListener(this);
        this.bnd.tvForgotPassword.setOnClickListener(this);
    }

    private TextWatcher getTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int usuario = bnd.etUser.getText().toString().length();
                int password = bnd.etPassword.getText().toString().length();
                bnd.btnLogin.setEnabled(usuario > 0 && password > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v.equals(this.bnd.btnLogin)) {
            this.verificarRed();

            ApiClient cliente = ICoreApiClient.getApiClient();
            LoginApi loginApi = new LoginApi(cliente);

            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setUsuario(bnd.etUser.getText().toString());
            loginInfo.setPassword(bnd.etPassword.getText().toString());

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
                    });
                }
            }).start();
        }

        if (v.equals(this.bnd.tvForgotPassword)) {
            this.bnd.tvError.setVisibility(View.GONE);
            String usuario = this.bnd.etUser.getText().toString().trim();
            Intent i = new Intent(getApplicationContext(), OlvideClaveActivity.class);
            i.putExtra("USUARIO", usuario);
            startActivity(i);
        }
    }
}