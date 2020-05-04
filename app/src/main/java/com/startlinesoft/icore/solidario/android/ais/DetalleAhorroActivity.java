package com.startlinesoft.icore.solidario.android.ais;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.startlinesoft.icore.solidario.android.ais.adapters.adapters.AhorroDetalleAdapter;
import com.startlinesoft.icore.solidario.android.ais.databinding.ActivityDetalleAhorroBinding;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreGeneral;
import com.startlinesoft.icore.solidario.api.models.DetalleAhorro;
import com.startlinesoft.icore.solidario.api.models.MovimientoAhorro;

import java.util.List;

public class DetalleAhorroActivity extends ICoreAppCompatActivity implements View.OnClickListener {

    private ActivityDetalleAhorroBinding bnd;
    private DetalleAhorro detalleAhorro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bnd = ActivityDetalleAhorroBinding.inflate(getLayoutInflater());
        setContentView(bnd.getRoot());

        //Se valida token activo
        this.validarLogin();

        detalleAhorro = (DetalleAhorro) getIntent().getSerializableExtra("AHORRO");

        setSupportActionBar(bnd.tbToolbar);
        bnd.tbToolbar.setNavigationIcon(R.drawable.ic_angle_left);
        bnd.tbToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bnd.ivImagen.setImageBitmap(ICoreGeneral.getSocioImagen());
        bnd.ivImagen.setOnClickListener(this);

        bnd.tvNombreModalidad.setText(detalleAhorro.getNombreModalidad());

        bnd.rvAhorrosDealle.setLayoutManager(new LinearLayoutManager(this));
        List<MovimientoAhorro> movimientosAhorros = detalleAhorro.getMovimientos();
        bnd.rvAhorrosDealle.setAdapter(new AhorroDetalleAdapter(movimientosAhorros));
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
