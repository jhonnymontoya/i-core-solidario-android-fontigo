package com.startlinesoft.icore.solidario.android.fontigo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.startlinesoft.icore.solidario.android.fontigo.adapters.adapters.CreditoDetalleAdapter;
import com.startlinesoft.icore.solidario.android.fontigo.databinding.ActivityDetallecreditoBinding;
import com.startlinesoft.icore.solidario.android.fontigo.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.android.fontigo.utilidades.ICoreGeneral;
import com.startlinesoft.icore.solidario.api.models.DetalleCredito;
import com.startlinesoft.icore.solidario.api.models.MovimientoCredito;

import java.util.List;

public class DetalleCreditoActivity extends ICoreAppCompatActivity implements View.OnClickListener {

    private ActivityDetallecreditoBinding bnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bnd = ActivityDetallecreditoBinding.inflate(this.getLayoutInflater());
        this.setContentView(this.bnd.getRoot());

        //Se valida token activo
        this.validarLogin();

        DetalleCredito detalleCredito = (DetalleCredito) getIntent().getSerializableExtra("CREDITO");

        this.setSupportActionBar(bnd.tbToolbar);
        this.bnd.tbToolbar.setNavigationIcon(R.drawable.ic_angle_left);
        this.bnd.tbToolbar.setNavigationOnClickListener(v -> this.finish());

        this.bnd.ivImagen.setImageBitmap(ICoreGeneral.getSocioImagen());
        this.bnd.ivImagen.setOnClickListener(this);

        this.bnd.tvNombreModalidad.setText(detalleCredito.getModalidad());

        bnd.rvCreditosDetalle.setLayoutManager(new LinearLayoutManager(this));
        List<MovimientoCredito> movimientosCreditos = detalleCredito.getMovimientosCreditos();
        bnd.rvCreditosDetalle.setAdapter(new CreditoDetalleAdapter(movimientosCreditos));
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        removerTituloBarra();
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