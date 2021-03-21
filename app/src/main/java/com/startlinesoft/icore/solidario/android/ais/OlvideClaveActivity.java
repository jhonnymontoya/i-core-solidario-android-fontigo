package com.startlinesoft.icore.solidario.android.ais;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.startlinesoft.icore.solidario.ApiClient;
import com.startlinesoft.icore.solidario.ApiException;
import com.startlinesoft.icore.solidario.android.ais.databinding.ActivityOlvideclaveBinding;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreApiClient;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.api.LoginApi;
import com.startlinesoft.icore.solidario.api.models.ForgotPassword;

public class OlvideClaveActivity extends ICoreAppCompatActivity implements View.OnClickListener {

    private ActivityOlvideclaveBinding bnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bnd = ActivityOlvideclaveBinding.inflate(this.getLayoutInflater());
        setContentView(this.bnd.getRoot());

        String usuario = this.getIntent().getStringExtra("USUARIO");
        this.bnd.etUser.setText(usuario);
        this.bnd.etUser.addTextChangedListener(this.getTextWatcher());

        this.bnd.btnSubmit.setOnClickListener(this);
        this.bnd.btnSubmit.setEnabled(usuario.length() > 0);

        this.bnd.tvVolver.setOnClickListener(this);
    }

    private TextWatcher getTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int user = bnd.etUser.getText().toString().length();
                bnd.btnSubmit.setEnabled(user > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v.equals(this.bnd.btnSubmit)) {
            ApiClient cliente = ICoreApiClient.getApiClient();
            LoginApi loginApi = new LoginApi(cliente);

            ForgotPassword forgotPassword = new ForgotPassword();
            String usuario = this.bnd.etUser.getText().toString().trim();
            forgotPassword.setUsuario(usuario);

            this.bnd.progressBar.setVisibility(View.VISIBLE);
            new Thread(() -> {
                try {
                    loginApi.sendResetLinkEmail(forgotPassword);
                } catch (ApiException ignored) {
                }
                bnd.progressBar.post(() -> {
                    bnd.progressBar.setVisibility(View.GONE);
                    finish();
                });
            }).start();
        }

        if (v.equals(bnd.tvVolver)) {
            this.finish();
        }
    }
}