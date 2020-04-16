package com.startlinesoft.icore.solidario.android.ais;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.startlinesoft.icore.solidario.ApiClient;
import com.startlinesoft.icore.solidario.ApiException;
import com.startlinesoft.icore.solidario.Configuration;
import com.startlinesoft.icore.solidario.android.ais.databinding.ActivityOlvideClaveBinding;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreApiClient;
import com.startlinesoft.icore.solidario.api.LoginApi;
import com.startlinesoft.icore.solidario.api.models.ForgotPassword;

import java.io.IOException;

public class OlvideClaveActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityOlvideClaveBinding bnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bnd = ActivityOlvideClaveBinding.inflate(getLayoutInflater());
        setContentView(bnd.getRoot());

        String usuario = getIntent().getStringExtra("USUARIO");
        bnd.etUser.setText(usuario);
        bnd.etUser.addTextChangedListener(getTextWatcher());

        bnd.btnSubmit.setOnClickListener(this);
        bnd.btnSubmit.setEnabled(usuario.length() > 0);

        bnd.tvVolver.setOnClickListener(this);
    }

    private TextWatcher getTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int user = bnd.etUser.getText().toString().trim().length();
                bnd.btnSubmit.setEnabled(user > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
    }

    @Override
    public void onClick(View v) {
        if(v.equals(bnd.btnSubmit)) {
            ApiClient cliente = ICoreApiClient.getApiClient();
            LoginApi loginApi = new LoginApi(cliente);

            ForgotPassword forgotPassword = new ForgotPassword();
            String usuario = bnd.etUser.getText().toString().trim();
            forgotPassword.setUsuario(usuario);

            bnd.progressBar.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        loginApi.sendResetLinkEmail(forgotPassword);
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }
                    bnd.progressBar.post(new Runnable() {
                        @Override
                        public void run() {
                            bnd.progressBar.setVisibility(View.GONE);
                            finish();
                        }
                    });
                }
            }).start();
        }

        if(v.equals(bnd.tvVolver)) {
            finish();
        }
    }
}