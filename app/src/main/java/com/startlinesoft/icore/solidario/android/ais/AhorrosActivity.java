package com.startlinesoft.icore.solidario.android.ais;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;

import com.startlinesoft.icore.solidario.android.ais.adapters.AhorroGeneralAdapter;
import com.startlinesoft.icore.solidario.android.ais.adapters.AhorroProgramadoAdapter;
import com.startlinesoft.icore.solidario.android.ais.adapters.SDATAdapter;
import com.startlinesoft.icore.solidario.android.ais.databinding.ActivityAhorrosBinding;
import com.startlinesoft.icore.solidario.android.ais.enums.TipoRecyclerViewItem;
import com.startlinesoft.icore.solidario.android.ais.listeners.ICoreRecyclerViewItemListener;
import com.startlinesoft.icore.solidario.android.ais.models.DetalleAhorroViewModel;
import com.startlinesoft.icore.solidario.android.ais.models.DetalleAhorroViewModelFactory;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreGeneral;
import com.startlinesoft.icore.solidario.api.models.AhorroGeneral;
import com.startlinesoft.icore.solidario.api.models.AhorroProgramado;
import com.startlinesoft.icore.solidario.api.models.Ahorros;
import com.startlinesoft.icore.solidario.api.models.DetalleAhorro;
import com.startlinesoft.icore.solidario.api.models.SDAT;
import com.startlinesoft.icore.solidario.api.models.Socio;

import java.util.List;

public class AhorrosActivity extends ICoreAppCompatActivity implements View.OnClickListener, ICoreRecyclerViewItemListener {

    private ActivityAhorrosBinding bnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bnd = ActivityAhorrosBinding.inflate(getLayoutInflater());
        setContentView(bnd.getRoot());

        //Se valida token activo
        this.validarLogin();

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

        bnd.tabHost.setup();

        TabHost.TabSpec tabSpec = bnd.tabHost.newTabSpec("generales");
        tabSpec.setIndicator(getText(R.string.ahorros_generales));
        tabSpec.setContent(R.id.tabGenerales);
        bnd.tabHost.addTab(tabSpec);

        tabSpec = bnd.tabHost.newTabSpec("programados");
        tabSpec.setIndicator(getText(R.string.ahorros_programados));
        tabSpec.setContent(R.id.tabProgramados);
        bnd.tabHost.addTab(tabSpec);

        tabSpec = bnd.tabHost.newTabSpec("sdats");
        tabSpec.setIndicator(getText(R.string.sdats));
        tabSpec.setContent(R.id.tabSDAT);
        bnd.tabHost.addTab(tabSpec);

        Ahorros ahorros = ICoreGeneral.getSocio().getAhorros();

        //Ahorros generales
        if(ahorros.getAhorrosGenerales().size() > 0) {
            bnd.rvAhorrosGenerales.setLayoutManager(new LinearLayoutManager(this));
            List<AhorroGeneral> ahorrosGenerales = ahorros.getAhorrosGenerales();
            AhorroGeneralAdapter aga = new AhorroGeneralAdapter(ahorrosGenerales);
            aga.setOnItemClickListener(this);
            bnd.rvAhorrosGenerales.setAdapter(aga);
        }
        else {
            bnd.tvGeneralesSinRegistros.setVisibility(View.VISIBLE);
        }

        //Ahorros programados
        if(ahorros.getAhorrosProgramados().size() > 0) {
            bnd.rvAhorrosProgramados.setLayoutManager(new LinearLayoutManager(this));
            List<AhorroProgramado> ahorrosProgramados = ahorros.getAhorrosProgramados();
            AhorroProgramadoAdapter apa = new AhorroProgramadoAdapter(ahorrosProgramados);
            apa.setOnItemClickListener(this);
            bnd.rvAhorrosProgramados.setAdapter(apa);
        }
        else {
            bnd.tvProgramadosSinRegistros.setVisibility(View.VISIBLE);
        }

        //SDATs
        if(ahorros.getSdATs().size() > 0) {
            bnd.rvSDAT.setLayoutManager(new LinearLayoutManager(this));
            List<SDAT> sdats = ahorros.getSdATs();
            SDATAdapter sa = new SDATAdapter(sdats);
            sa.setOnItemClickListener(this);
            bnd.rvSDAT.setAdapter(sa);
        }
        else {
            bnd.tvSDATSinRegistros.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Título de la entidad
        bnd.tbToolbar.setTitle(getString(R.string.ahorros));
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

    @Override
    public void onRecyclerViewItemClick(View v, int posicion, Integer id, TipoRecyclerViewItem tipo) {
        System.out.println("v: " + v.getClass().toString());
        System.out.println("pos: " + posicion);
        System.out.println("ID: " + id);
        System.out.println("Tipo: " + tipo);

        //Se valida token activo
        this.validarLogin();

        if(tipo == TipoRecyclerViewItem.SDAT) {
            //TODO: Solo SDAT
        }

        if(tipo == TipoRecyclerViewItem.AHORRO_PROGRAMADO || tipo == TipoRecyclerViewItem.AHORRO_GENERAL) {
            final DetalleAhorro[] detalleAhorro = new DetalleAhorro[1];
            DetalleAhorroViewModel detalleAhorroViewModel = new ViewModelProvider(
                    getViewModelStore(),
                    new DetalleAhorroViewModelFactory(id)
            ).get(DetalleAhorroViewModel.class);

            bnd.progressBar.setVisibility(View.VISIBLE);
            detalleAhorroViewModel.getDetalleAhorro().observe(this, new Observer<DetalleAhorro>() {
                @Override
                public void onChanged(DetalleAhorro detalleAhorros) {
                    detalleAhorro[0] = detalleAhorros;
                    bnd.progressBar.setVisibility(View.GONE);
                    System.out.println(detalleAhorro[0]);
                }
            });
        }

        //TODO: Implementar opción
    }
}
