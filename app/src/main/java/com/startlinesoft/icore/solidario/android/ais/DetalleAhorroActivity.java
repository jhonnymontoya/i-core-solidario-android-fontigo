package com.startlinesoft.icore.solidario.android.ais;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.startlinesoft.icore.solidario.android.ais.databinding.ActivityDetalleAhorroBinding;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.api.models.DetalleAhorro;
import com.startlinesoft.icore.solidario.api.models.Socio;

public class DetalleAhorroActivity extends ICoreAppCompatActivity implements View.OnClickListener {

    private ActivityDetalleAhorroBinding bnd;
    private DetalleAhorro detalleAhorro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bnd = ActivityDetalleAhorroBinding.inflate(getLayoutInflater());
        setContentView(bnd.getRoot());
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        removerTituloBarra();
    }

    @Override
    public void onClick(View v) {
        // Ir a info de cuenta
        if(v.equals(bnd.ivImagen)) {
            Intent i = new Intent(this, InfoActivity.class);
            startActivity(i);
            return;
        }
    }

}
