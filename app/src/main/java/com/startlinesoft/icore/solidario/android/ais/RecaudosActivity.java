package com.startlinesoft.icore.solidario.android.ais;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;

import com.startlinesoft.icore.solidario.android.ais.adapters.RecaudoAdapter;
import com.startlinesoft.icore.solidario.android.ais.databinding.ActivityRecaudosBinding;
import com.startlinesoft.icore.solidario.android.ais.enums.TipoRecyclerViewItem;
import com.startlinesoft.icore.solidario.android.ais.listeners.ICoreRecyclerViewItemListener;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreGeneral;
import com.startlinesoft.icore.solidario.api.models.Recaudo;
import com.startlinesoft.icore.solidario.api.models.Socio;

import java.util.List;

public class RecaudosActivity extends ICoreAppCompatActivity implements View.OnClickListener, ICoreRecyclerViewItemListener {

    private ActivityRecaudosBinding bnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bnd = ActivityRecaudosBinding.inflate(getLayoutInflater());
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

        TabHost.TabSpec tabSpec = bnd.tabHost.newTabSpec("recaudos");
        tabSpec.setIndicator(getText(R.string.recaudos_nomina));
        tabSpec.setContent(R.id.tabRecaudos);
        bnd.tabHost.addTab(tabSpec);

        List<Recaudo> recaudos = ICoreGeneral.getSocio().getRecaudo();

        if(recaudos.size() > 0) {
            bnd.rvRecaudos.setLayoutManager(new LinearLayoutManager(this));
            RecaudoAdapter adapter = new RecaudoAdapter(recaudos);
            adapter.setOnItemClickListener(this);
            bnd.rvRecaudos.setAdapter(adapter);
        }
        else {
            bnd.tvRecaudosSinRegistros.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        bnd.tbToolbar.setTitle(getString(R.string.recaudos_nomina));
    }

    @Override
    public void onClick(View v) {

        // Ir a info de cuenta
        if(v.equals(bnd.ivImagen)) {
            Intent i = new Intent(this, InfoActivity.class);
            startActivity(i);
            return;
        }

        //TODO: Implementar acciones aqui
    }

    @Override
    public void onRecyclerViewItemClick(View v, int posicion, Integer id, TipoRecyclerViewItem tipo) {
        Intent i = new Intent(this, DetalleRecaudoActivity.class);
        i.putExtra("RECAUDO", ICoreGeneral.getSocio().getRecaudo().get(posicion));
        startActivity(i);
        return;
    }
}
