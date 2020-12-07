package com.startlinesoft.icore.solidario.android.ais;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreApiClient;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;

public class SplashActivity extends ICoreAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        this.verificarRed();

        if (this.isSetToken() == true) {
            ICoreApiClient.setToken(this.getToken());
            boolean esTokenValido = ICoreApiClient.esTokenValido();
            Intent i;
            if(esTokenValido == true){
                //Sí es válido, ir a dashboard
                i = new Intent(getBaseContext(), DashBoardActivity.class);
            }
            else{
                //No es válido, ir al login
                i = new Intent(getBaseContext(), LoginActivity.class);
                this.removerToken();
            }
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(i);
            finish();
        } else {
            Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            this.removerToken();
            this.startActivity(i);
            finish();
        }
    }
}