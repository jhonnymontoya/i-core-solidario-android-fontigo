package com.startlinesoft.icore.solidario.android.ais;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.startlinesoft.icore.solidario.android.ais.adapters.AhorroGeneralAdapter;
import com.startlinesoft.icore.solidario.android.ais.adapters.AhorroProgramadoAdapter;
import com.startlinesoft.icore.solidario.android.ais.adapters.SDATAdapter;
import com.startlinesoft.icore.solidario.android.ais.databinding.ActivityAhorrosBinding;
import com.startlinesoft.icore.solidario.android.ais.enums.TipoRecyclerViewItem;
import com.startlinesoft.icore.solidario.android.ais.listeners.ICoreRecyclerViewItemListener;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.api.models.AhorroGeneral;
import com.startlinesoft.icore.solidario.api.models.AhorroProgramado;
import com.startlinesoft.icore.solidario.api.models.Ahorros;
import com.startlinesoft.icore.solidario.api.models.SDAT;
import com.startlinesoft.icore.solidario.api.models.Socio;

import java.util.List;

public class AhorrosActivity extends ICoreAppCompatActivity implements View.OnClickListener, ICoreRecyclerViewItemListener {

    private ActivityAhorrosBinding bnd;
    private Socio socio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bnd = ActivityAhorrosBinding.inflate(getLayoutInflater());
        setContentView(bnd.getRoot());

        //Se valida token activo
        this.validarLogin();

        socio = (Socio) getIntent().getSerializableExtra("SOCIO");

        setSupportActionBar(bnd.tbToolbar);
        bnd.tbToolbar.setNavigationIcon(R.drawable.ic_angle_left);
        bnd.tbToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bitmap bitmap = BitmapFactory.decodeByteArray(
                socio.getImagen(),
                0,
                socio.getImagen().length
        );
        bnd.ivImagen.setImageBitmap(bitmap);
        bnd.ivImagen.setOnClickListener(this);

        Ahorros ahorros = socio.getAhorros();

        //Ahorros generales
        bnd.rvAhorrosGenerales.setLayoutManager(new LinearLayoutManager(this));
        List<AhorroGeneral> ahorrosGenerales = ahorros.getAhorrosGenerales();
        AhorroGeneralAdapter aga = new AhorroGeneralAdapter(ahorrosGenerales);
        aga.setOnItemClickListener(this);
        bnd.rvAhorrosGenerales.setAdapter(aga);

        //Ahorros programados
        if(ahorros.getAhorrosProgramados().size() > 0) {
            bnd.rvAhorrosProgramados.setLayoutManager(new LinearLayoutManager(this));
            List<AhorroProgramado> ahorrosProgramados = ahorros.getAhorrosProgramados();
            AhorroProgramadoAdapter apa = new AhorroProgramadoAdapter(ahorrosProgramados);
            apa.setOnItemClickListener(this);
            bnd.rvAhorrosProgramados.setAdapter(apa);
            bnd.cvAhorrosProgramados.setVisibility(View.VISIBLE);
        }

        //SDATs
        if(ahorros.getSdATs().size() > 0) {
            bnd.rvSDAT.setLayoutManager(new LinearLayoutManager(this));
            List<SDAT> sdats = ahorros.getSdATs();
            SDATAdapter sa = new SDATAdapter(sdats);
            sa.setOnItemClickListener(this);
            bnd.rvSDAT.setAdapter(sa);
            bnd.cvSDAT.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //removerTituloBarra();
        // Título de la entidad
        bnd.tbToolbar.setTitle(getString(R.string.ahorros));
    }

    @Override
    public void onClick(View v) {

        // Ir a info de cuenta
        if(v.equals(bnd.ivImagen)) {
            Intent i = new Intent(this, InfoActivity.class);
            i.putExtra("SOCIO", socio);
            startActivity(i);
            return;
        }

        //TODO: Implementar acciones aqui
    }

    @Override
    public void onRecyclerViewItemClick(View v, int posicion, Integer id, TipoRecyclerViewItem tipo) {
        System.out.println("v: " + v.getClass().toString());
        System.out.println("pos: " + posicion);
        System.out.println("ID: " + id);
        System.out.println("Tipo: " + tipo);

        //TODO: Implementar opción
    }
}
