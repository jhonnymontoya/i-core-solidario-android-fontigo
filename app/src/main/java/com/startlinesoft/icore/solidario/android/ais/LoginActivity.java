package com.startlinesoft.icore.solidario.android.ais;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.startlinesoft.icore.solidario.android.ais.databinding.ActivityLoginBinding;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;

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
        super.onClick(v);
        Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show();
    }
}