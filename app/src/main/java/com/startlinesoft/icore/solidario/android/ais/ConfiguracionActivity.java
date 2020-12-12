package com.startlinesoft.icore.solidario.android.ais;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.startlinesoft.icore.solidario.android.ais.databinding.ActivityConfiguracionBinding;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;

public class ConfiguracionActivity extends ICoreAppCompatActivity {

    private ActivityConfiguracionBinding bnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bnd = ActivityConfiguracionBinding.inflate(this.getLayoutInflater());
        this.setContentView(this.bnd.getRoot());

        this.validarDataTouchId();

        //Se valida token activo
        this.validarLogin();

        this.setSupportActionBar(this.bnd.tbToolbar);
        this.getSupportActionBar().setTitle(R.string.configuracion);
        this.bnd.tbToolbar.setNavigationIcon(R.drawable.ic_angle_left);
        this.bnd.tbToolbar.setNavigationOnClickListener(v -> this.finish());

        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.configuracion_container, new ConfiguracionFragment())
                .commit();
    }
}