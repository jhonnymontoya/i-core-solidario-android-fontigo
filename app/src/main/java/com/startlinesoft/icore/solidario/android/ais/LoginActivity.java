package com.startlinesoft.icore.solidario.android.ais;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.startlinesoft.icore.solidario.ApiClient;
import com.startlinesoft.icore.solidario.ApiException;
import com.startlinesoft.icore.solidario.android.ais.databinding.LoginMainBinding;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreApiClient;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.api.LoginApi;
import com.startlinesoft.icore.solidario.api.models.LoginInfo;
import com.startlinesoft.icore.solidario.api.models.LoginToken;

public class LoginActivity extends ICoreAppCompatActivity implements View.OnClickListener {

    private LoginMainBinding bnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bnd = LoginMainBinding.inflate(getLayoutInflater());
        setContentView(bnd.getRoot());

        bnd.etUser.addTextChangedListener(getTextWatcher());
        bnd.etPassword.addTextChangedListener(getTextWatcher());
        bnd.btnLogin.setOnClickListener(this);
        bnd.tvForgotPassword.setOnClickListener(this);
    }

    private TextWatcher getTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int usuario = bnd.etUser.getText().toString().length();
                int password = bnd.etPassword.getText().toString().length();
                bnd.btnLogin.setEnabled(usuario > 0 && password > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
    }

    @Override
    public void onClick(View v) {
        if(v.equals(bnd.btnLogin)) {
            this.verificarRed();

            ApiClient cliente = ICoreApiClient.getApiClient();
            LoginApi loginApi = new LoginApi(cliente);

            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setUsuario(bnd.etUser.getText().toString());
            loginInfo.setPassword(bnd.etPassword.getText().toString());

            bnd.progressBar.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        LoginToken loginToken = loginApi.login(loginInfo);
                        putToken(loginToken.getToken());
                        ICoreApiClient.setToken(loginToken.getToken());

                        Intent i = new Intent(getBaseContext(), DashBoardActivity.class);
                        startActivity(i);
                        finish();
                    } catch (ApiException e) {
                        bnd.tvErrror.post(new Runnable() {
                            @Override
                            public void run() {
                                bnd.tvErrror.setVisibility(View.VISIBLE);
                                bnd.progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            }).start();
        }

        if(v.equals(bnd.tvForgotPassword)) {
            bnd.tvErrror.setVisibility(View.GONE);
            String usuario = bnd.etUser.getText().toString().trim();
            Intent i = new Intent(getApplicationContext(), OlvideClaveActivity.class);
            i.putExtra("USUARIO", usuario);
            startActivity(i);
        }
    }
}
