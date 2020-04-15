package com.startlinesoft.icore.solidario.android.ais;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.startlinesoft.icore.solidario.android.ais.databinding.LoginMainBinding;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

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
            bnd.progressBar.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                int progressStatus = 0;
                @Override
                public void run() {
                    while(progressStatus < 50) {
                        progressStatus += 1;
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                        }
                    }
                    bnd.progressBar.post(new Runnable() {
                        @Override
                        public void run() {
                            bnd.progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }).start();
            bnd.tvErrror.setVisibility(View.VISIBLE);
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
