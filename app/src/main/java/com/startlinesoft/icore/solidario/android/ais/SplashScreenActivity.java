package com.startlinesoft.icore.solidario.android.ais;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreApiClient;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;

public class SplashScreenActivity extends ICoreAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        this.verificarRed();

        if(this.isSetToken()) {
            ICoreApiClient.setToken(this.getToken());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean esTokenValido =  ICoreApiClient.esTokenValido();
                    Intent i;
                    if(esTokenValido) {
                        //Si es válido, e ir a dashboard
                        i = new Intent(getBaseContext(), DashBoardActivity.class);
                    }
                    else {
                        //No es válido e ir al login
                        i = new Intent(getBaseContext(), LoginActivity.class);
                        removeToken();
                    }

                    startActivity(i);
                    finish();
                }
            }).start();
        }
        else {
            Intent i = new Intent(this, LoginActivity.class);
            this.removeToken();
            startActivity(i);
            finish();
        }
    }
}
