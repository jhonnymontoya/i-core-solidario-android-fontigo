package com.startlinesoft.icore.solidario.android.ais;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.startlinesoft.icore.solidario.android.ais.databinding.ActivityDetallesdatBinding;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreGeneral;
import com.startlinesoft.icore.solidario.api.models.SDAT;

public class DetalleSDATActivity extends ICoreAppCompatActivity implements View.OnClickListener {

    private ActivityDetallesdatBinding bnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bnd = ActivityDetallesdatBinding.inflate(this.getLayoutInflater());
        this.setContentView(this.bnd.getRoot());

        //Se valida token activo
        this.validarLogin();

        SDAT sdat = (SDAT) getIntent().getSerializableExtra("SDAT");

        setSupportActionBar(this.bnd.tbToolbar);
        this.bnd.tbToolbar.setNavigationIcon(R.drawable.ic_angle_left);
        this.bnd.tbToolbar.setNavigationOnClickListener(v -> finish());

        this.bnd.ivImagen.setImageBitmap(ICoreGeneral.getSocioImagen());
        this.bnd.ivImagen.setOnClickListener(this);

        this.bnd.tvNombreSDAT.setText(String.format("%s %s", sdat.getTipo(), sdat.getNumeroDeposito()));

        this.bnd.tvNumeroDeposito.setText(sdat.getNumeroDeposito().toString());
        this.bnd.tvTipo.setText(sdat.getTipo());
        this.bnd.tvValor.setText(String.format("$%s", sdat.getValor()));
        this.bnd.tvFechaConstitucion.setText(ICoreGeneral.reverseFecha(sdat.getFechaConstitucion().toString()));
        this.bnd.tvPlazo.setText(sdat.getPlazoDias());
        this.bnd.tvFechaVencimiento.setText(ICoreGeneral.reverseFecha(sdat.getFechaVencimiento().toString()));
        this.bnd.tvTasa.setText(String.format("%s%%", sdat.getTasaEA()));
        this.bnd.tvSaldo.setText(String.format("$%s", sdat.getSaldo()));
        this.bnd.tvInteresesReconocidos.setText(String.format("$%s", sdat.getInteresesReconocidos()));
        this.bnd.tvEstado.setText(sdat.getEstado());
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.removerTituloBarra();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}