package com.startlinesoft.icore.solidario.android.ais;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.startlinesoft.icore.solidario.android.ais.databinding.ActivitySimuladorBinding;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreGeneral;

public class SimuladorActivity extends ICoreAppCompatActivity implements View.OnClickListener {

    private ActivitySimuladorBinding bnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bnd = ActivitySimuladorBinding.inflate(this.getLayoutInflater());
        this.setContentView(this.bnd.getRoot());

        //Se valida token activo
        this.validarLogin();

        this.setSupportActionBar(this.bnd.tbToolbar);
        this.bnd.tbToolbar.setNavigationIcon(R.drawable.ic_angle_left);
        this.bnd.tbToolbar.setNavigationOnClickListener(v -> this.finish());

        this.bnd.ivImagen.setImageBitmap(ICoreGeneral.getSocioImagen());
        this.bnd.ivImagen.setOnClickListener(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.bnd.tbToolbar.setTitle(getString(R.string.accion_simulador));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        // Ir a info de cuenta
        if (v.equals(bnd.ivImagen)) {
            Intent i = new Intent(this, InfoActivity.class);
            startActivity(i);
            return;
        }
    }
}