package com.startlinesoft.icore.solidario.android.ais;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;

import com.startlinesoft.icore.solidario.ApiException;
import com.startlinesoft.icore.solidario.android.ais.adapters.adapters.CodeudaAdapter;
import com.startlinesoft.icore.solidario.android.ais.adapters.adapters.CreditoAdapter;
import com.startlinesoft.icore.solidario.android.ais.databinding.ActivityCreditosBinding;
import com.startlinesoft.icore.solidario.android.ais.enums.TipoRecyclerViewItem;
import com.startlinesoft.icore.solidario.android.ais.listeners.ICoreRecyclerViewItemListener;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreApiClient;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreGeneral;
import com.startlinesoft.icore.solidario.api.CreditosApi;
import com.startlinesoft.icore.solidario.api.models.Codeuda;
import com.startlinesoft.icore.solidario.api.models.Credito;
import com.startlinesoft.icore.solidario.api.models.Creditos;
import com.startlinesoft.icore.solidario.api.models.DetalleCredito;

import java.util.List;

public class CreditosActivity extends ICoreAppCompatActivity implements View.OnClickListener, ICoreRecyclerViewItemListener {

    private ActivityCreditosBinding bnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bnd = ActivityCreditosBinding.inflate(this.getLayoutInflater());
        this.setContentView(this.bnd.getRoot());

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

        TabHost.TabSpec tabSpec = bnd.tabHost.newTabSpec("creditos");
        tabSpec.setIndicator(getText(R.string.creditos));
        tabSpec.setContent(R.id.tabCreditos);
        bnd.tabHost.addTab(tabSpec);

        tabSpec = bnd.tabHost.newTabSpec("codeudas");
        tabSpec.setIndicator(getText(R.string.codeudas));
        tabSpec.setContent(R.id.tabCodeudas);
        bnd.tabHost.addTab(tabSpec);

        Creditos listaCreditos = ICoreGeneral.getSocio().getCreditos();

        //Créditos
        if (listaCreditos.getCreditos().size() > 0) {
            bnd.rvCreditos.setLayoutManager(new LinearLayoutManager(this));
            List<Credito> creditos = listaCreditos.getCreditos();
            CreditoAdapter ca = new CreditoAdapter(creditos);
            ca.setOnItemClickListener(this);
            bnd.rvCreditos.setAdapter(ca);
        } else {
            bnd.tvCreditosSinRegistros.setVisibility(View.VISIBLE);
        }

        //Codeudas
        if (listaCreditos.getCodeudas().size() > 0) {
            bnd.rvCodeudas.setLayoutManager(new LinearLayoutManager(this));
            List<Codeuda> codeudas = listaCreditos.getCodeudas();
            bnd.rvCodeudas.setAdapter(new CodeudaAdapter(codeudas));
        } else {
            bnd.tvCodeudasSinRegistros.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Título de la entidad
        bnd.tbToolbar.setTitle(getString(R.string.creditos));
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

    @Override
    public void onRecyclerViewItemClick(View v, int posicion, Integer id, TipoRecyclerViewItem tipo) {

        //Se valida token activo
        this.validarLogin();

        // Créditos
        if (tipo == TipoRecyclerViewItem.CREDITO) {
            CreditosApi creditosApi = new CreditosApi(ICoreApiClient.getApiClient());
            bnd.progressBar.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        DetalleCredito detalleCredito = creditosApi.obtenerCredito(id);
                        bnd.progressBar.post(new Runnable() {
                            @Override
                            public void run() {
                                bnd.progressBar.setVisibility(View.GONE);
                                Intent i = new Intent(getBaseContext(), DetalleCreditoActivity.class);
                                i.putExtra("CREDITO", detalleCredito);
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
                    }
                }
            }).start();
        }
    }
}