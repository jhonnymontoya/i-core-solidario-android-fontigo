package com.startlinesoft.icore.solidario.android.fontigo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.startlinesoft.icore.solidario.ApiException;
import com.startlinesoft.icore.solidario.android.fontigo.adapters.adapters.AhorroGeneralAdapter;
import com.startlinesoft.icore.solidario.android.fontigo.adapters.adapters.AhorroProgramadoAdapter;
import com.startlinesoft.icore.solidario.android.fontigo.adapters.adapters.SDATAdapter;
import com.startlinesoft.icore.solidario.android.fontigo.databinding.ActivityAhorrosBinding;
import com.startlinesoft.icore.solidario.android.fontigo.enums.TipoRecyclerViewItem;
import com.startlinesoft.icore.solidario.android.fontigo.listeners.ICoreRecyclerViewItemListener;
import com.startlinesoft.icore.solidario.android.fontigo.utilidades.ICoreApiClient;
import com.startlinesoft.icore.solidario.android.fontigo.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.android.fontigo.utilidades.ICoreGeneral;
import com.startlinesoft.icore.solidario.api.AhorrosApi;
import com.startlinesoft.icore.solidario.api.models.AhorroGeneral;
import com.startlinesoft.icore.solidario.api.models.AhorroProgramado;
import com.startlinesoft.icore.solidario.api.models.Ahorros;
import com.startlinesoft.icore.solidario.api.models.DetalleAhorro;
import com.startlinesoft.icore.solidario.api.models.SDAT;

import java.util.List;

public class AhorrosActivity extends ICoreAppCompatActivity implements View.OnClickListener, ICoreRecyclerViewItemListener {

    private ActivityAhorrosBinding bnd;
    private List<SDAT> sdats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bnd = ActivityAhorrosBinding.inflate(this.getLayoutInflater());
        this.setContentView(this.bnd.getRoot());

        //Se valida token activo
        this.validarLogin();

        this.setSupportActionBar(this.bnd.tbToolbar);
        this.bnd.tbToolbar.setNavigationIcon(R.drawable.ic_angle_left);
        this.bnd.tbToolbar.setNavigationOnClickListener(v -> this.finish());

        this.bnd.ivImagen.setImageBitmap(ICoreGeneral.getSocioImagen());
        this.bnd.ivImagen.setOnClickListener(this);

        this.bnd.tabHost.setup();

        TabHost.TabSpec tabSpec = this.bnd.tabHost.newTabSpec("generales");
        tabSpec.setIndicator(getText(R.string.ahorros_generales));
        tabSpec.setContent(R.id.tabGenerales);
        this.bnd.tabHost.addTab(tabSpec);

        tabSpec = this.bnd.tabHost.newTabSpec("programados");
        tabSpec.setIndicator(getText(R.string.ahorros_programados));
        tabSpec.setContent(R.id.tabProgramados);
        this.bnd.tabHost.addTab(tabSpec);

        tabSpec = this.bnd.tabHost.newTabSpec("sdats");
        tabSpec.setIndicator(getText(R.string.sdats));
        tabSpec.setContent(R.id.tabSDAT);
        this.bnd.tabHost.addTab(tabSpec);

        Ahorros ahorros = ICoreGeneral.getSocio().getAhorros();

        //Ahorros generales
        if (ahorros.getAhorrosGenerales().size() > 0) {
            this.bnd.rvAhorrosGenerales.setLayoutManager(new LinearLayoutManager(this));
            List<AhorroGeneral> ahorrosGenerales = ahorros.getAhorrosGenerales();
            AhorroGeneralAdapter aga = new AhorroGeneralAdapter(ahorrosGenerales);
            aga.setOnItemClickListener(this);
            this.bnd.rvAhorrosGenerales.setAdapter(aga);
        } else {
            this.bnd.tvGeneralesSinRegistros.setVisibility(View.VISIBLE);
        }

        //Ahorros programados
        if (ahorros.getAhorrosProgramados().size() > 0) {
            this.bnd.rvAhorrosProgramados.setLayoutManager(new LinearLayoutManager(this));
            List<AhorroProgramado> ahorrosProgramados = ahorros.getAhorrosProgramados();
            AhorroProgramadoAdapter apa = new AhorroProgramadoAdapter(ahorrosProgramados);
            apa.setOnItemClickListener(this);
            this.bnd.rvAhorrosProgramados.setAdapter(apa);
        } else {
            this.bnd.tvProgramadosSinRegistros.setVisibility(View.VISIBLE);
        }

        //SDATs
        if (ahorros.getSdATs().size() > 0) {
            this.bnd.rvSDAT.setLayoutManager(new LinearLayoutManager(this));
            sdats = ahorros.getSdATs();
            SDATAdapter sa = new SDATAdapter(sdats);
            sa.setOnItemClickListener(this);
            this.bnd.rvSDAT.setAdapter(sa);
        } else {
            this.bnd.tvSDATSinRegistros.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.bnd.tbToolbar.setTitle(getString(R.string.ahorros));
    }

    @Override
    public void onRecyclerViewItemClick(View v, int posicion, Integer id, TipoRecyclerViewItem tipo) {
        if (this.isVibradorActivado()) {
            this.vibrar();
        }

        //Se valida token activo
        this.validarLogin();

        if (tipo == TipoRecyclerViewItem.SDAT) {
            SDAT sdat = sdats.get(posicion);
            Intent i = new Intent(getBaseContext(), DetalleSDATActivity.class);
            i.putExtra("SDAT", sdat);
            startActivity(i);
        }

        if (tipo == TipoRecyclerViewItem.AHORRO_PROGRAMADO || tipo == TipoRecyclerViewItem.AHORRO_GENERAL) {
            AhorrosApi ahorrosApi = new AhorrosApi(ICoreApiClient.getApiClient());
            bnd.progressBar.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        DetalleAhorro detalleAhorro = ahorrosApi.obtenerAhorro(id);
                        bnd.progressBar.post(new Runnable() {
                            @Override
                            public void run() {
                                bnd.progressBar.setVisibility(View.GONE);
                                Intent i = new Intent(getBaseContext(), DetalleAhorroActivity.class);
                                i.putExtra("AHORRO", detalleAhorro);
                                startActivity(i);
                            }
                        });
                    } catch (ApiException e) {
                        bnd.progressBar.post(new Runnable() {
                            @Override
                            public void run() {
                                bnd.progressBar.setVisibility(View.GONE);
                            }
                        });
                        e.printStackTrace();
                    }
                }
            }).start();
        }
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