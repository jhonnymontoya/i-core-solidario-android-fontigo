package com.startlinesoft.icore.solidario.android.ais;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.startlinesoft.icore.solidario.android.ais.adapters.adapters.CreditoDetalleAdapter;
import com.startlinesoft.icore.solidario.android.ais.databinding.ActivityDetalleCreditoBinding;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreGeneral;
import com.startlinesoft.icore.solidario.api.models.DetalleCredito;
import com.startlinesoft.icore.solidario.api.models.MovimientoCredito;

import java.util.List;

public class DetalleCreditoActivity extends ICoreAppCompatActivity implements View.OnClickListener {

    private ActivityDetalleCreditoBinding bnd;
    private DetalleCredito detalleCredito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bnd = ActivityDetalleCreditoBinding.inflate(getLayoutInflater());
        setContentView(bnd.getRoot());

        //Se valida token activo
        this.validarLogin();

        detalleCredito = (DetalleCredito) getIntent().getSerializableExtra("CREDITO");

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

        bnd.tvNombreModalidad.setText(detalleCredito.getModalidad());

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
        // Ir a info de cuenta
        if(v.equals(bnd.ivImagen)) {
            Intent i = new Intent(this, InfoActivity.class);
            startActivity(i);
            return;
        }
    }
}
