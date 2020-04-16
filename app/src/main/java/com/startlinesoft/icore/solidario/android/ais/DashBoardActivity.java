package com.startlinesoft.icore.solidario.android.ais;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.startlinesoft.icore.solidario.ApiClient;
import com.startlinesoft.icore.solidario.ApiException;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreApiClient;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.api.LoginApi;
import com.startlinesoft.icore.solidario.api.models.LoginInfo;

public class DashBoardActivity extends ICoreAppCompatActivity implements View.OnClickListener {

    private Button boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        this.boton = (Button)this.findViewById(R.id.btnSalir);
        this.boton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.verificarRed();

        ApiClient cliente = ICoreApiClient.getApiClient();
        LoginApi loginApi = new LoginApi(cliente);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    loginApi.logout();
                } catch (ApiException e) {
                }
                boton.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
            }
        }).start();
    }
}
