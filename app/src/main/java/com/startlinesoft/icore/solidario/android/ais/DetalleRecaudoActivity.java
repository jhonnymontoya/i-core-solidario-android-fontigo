package com.startlinesoft.icore.solidario.android.ais;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.startlinesoft.icore.solidario.android.ais.adapters.adapters.RecaudoDetalleAdapter;
import com.startlinesoft.icore.solidario.android.ais.databinding.ActivityDetalleRecaudoBinding;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreGeneral;
import com.startlinesoft.icore.solidario.api.models.ConceptoRecaudo;
import com.startlinesoft.icore.solidario.api.models.Recaudo;

import java.util.List;

public class DetalleRecaudoActivity extends ICoreAppCompatActivity implements View.OnClickListener {

    private ActivityDetalleRecaudoBinding bnd;
    private Recaudo recaudo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bnd = ActivityDetalleRecaudoBinding.inflate(getLayoutInflater());
        setContentView(bnd.getRoot());

        //Se valida token activo
        this.validarLogin();

        recaudo = (Recaudo) getIntent().getSerializableExtra("RECAUDO");

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

        String fecha = ICoreGeneral.reverseFecha(recaudo.getFechaRecaudo());
        bnd.tvFechaAplicacion.setText(fecha);
        bnd.tvTotalAplicado.setText(String.format("$%s", recaudo.getTotalAplicado()));

        bnd.rvRecaudosDealle.setLayoutManager(new LinearLayoutManager(this));
        List<ConceptoRecaudo> conceptos = recaudo.getConceptos();
        bnd.rvRecaudosDealle.setAdapter(new RecaudoDetalleAdapter(conceptos));
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
