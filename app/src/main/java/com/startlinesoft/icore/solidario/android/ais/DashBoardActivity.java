package com.startlinesoft.icore.solidario.android.ais;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.startlinesoft.icore.solidario.android.ais.databinding.ActivityDashBoardBinding;
import com.startlinesoft.icore.solidario.android.ais.models.SocioViewModel;
import com.startlinesoft.icore.solidario.android.ais.models.SocioViewModelFactory;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreGeneral;
import com.startlinesoft.icore.solidario.api.models.Ahorros;
import com.startlinesoft.icore.solidario.api.models.Creditos;
import com.startlinesoft.icore.solidario.api.models.Recaudo;
import com.startlinesoft.icore.solidario.api.models.Socio;

public class DashBoardActivity extends ICoreAppCompatActivity implements View.OnClickListener, MenuItem.OnMenuItemClickListener {

    private ActivityDashBoardBinding bnd;
    private SocioViewModel socioViewModel;
    private Socio socio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bnd = ActivityDashBoardBinding.inflate(getLayoutInflater());
        setContentView(bnd.getRoot());

        //Se valida token activo
        this.validarLogin();

        setSupportActionBar(bnd.tbToolbar);

        socioViewModel = new ViewModelProvider(getViewModelStore(), new SocioViewModelFactory())
                .get(SocioViewModel.class);

        bnd.progressBar.setVisibility(View.VISIBLE);
        socioViewModel.getSocio().observe(this, new Observer<Socio>() {
            @Override
            public void onChanged(Socio socio) {
                DashBoardActivity.this.socio = socio;
                bnd.progressBar.setVisibility(View.GONE);
                Bitmap bitmap = BitmapFactory.decodeByteArray(
                        socio.getImagen(),
                        0,
                        socio.getImagen().length
                );
                Ahorros ahorros = socio.getAhorros();
                Creditos creditos = socio.getCreditos();

                // Título de la entidad
                bnd.tbToolbar.setTitle(socio.getSiglaEntidad());

                bnd.ivImagen.setImageBitmap(bitmap);

                bnd.tvTotalAhorros.setText(String.format("$%s", ahorros.getTotalAhorros()));
                bnd.pbPorcentajeIncremento.setProgress(ahorros.getPorcentajeIncremento());
                bnd.tvPorcentajeIncremento.setText(String.format("%s%%", ahorros.getPorcentajeIncremento()));

                if(creditos.getTotalSaldoCapital().contentEquals("0") == false || creditos.getCodeudas().size() > 0) {
                    bnd.cvCreditos.setVisibility(View.VISIBLE);
                    bnd.tvTotalCreditos.setText(String.format("$%s", creditos.getTotalSaldoCapital()));
                    bnd.pbPorcentajeAbonado.setProgress(creditos.getPorcentajeAbonado());
                    bnd.tvPorcentajeAbonado.setText(String.format("%s%%", creditos.getPorcentajeAbonado()));
                }

                if(socio.getRecaudo().size() > 0) {
                    bnd.cvRecaudos.setVisibility(View.VISIBLE);
                    Recaudo recaudo = socio.getRecaudo().get(0);
                    bnd.tvTotalAplicado.setText(String.format("$%s", recaudo.getTotalAplicado()));
                    bnd.tvFechaAplicacion.setText(
                            ICoreGeneral.reverseFecha(recaudo.getFechaRecaudo())
                    );
                }
                else {
                    bnd.cvRecaudos.setVisibility(View.GONE);
                    bnd.tvTotalAplicado.setText(String.format("$0"));
                    bnd.tvFechaAplicacion.setText(String.format("00-00-0000"));
                }

            }
        });

        bnd.ivImagen.setOnClickListener(this);

        bnd.cvAhorros.setOnClickListener(this);
        bnd.cvCreditos.setOnClickListener(this);
        bnd.cvRecaudos.setOnClickListener(this);

        // Barra de navegación inferior
        bnd.bnvMenu.getMenu().setGroupCheckable(0, true, false);
        for(int i = 0;  i< bnd.bnvMenu.getMenu().size(); i++) {
            bnd.bnvMenu.getMenu().getItem(i).setChecked(true);
            bnd.bnvMenu.getMenu().getItem(i).setOnMenuItemClickListener(this);
        }
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

        // Ir a Ahorros
        if(v.equals(bnd.cvAhorros)) {
            Intent i = new Intent(this, AhorrosActivity.class);
            i.putExtra("SOCIO", socio);
            startActivity(i);
            return;
        }

        // Ir a Créditos
        if(v.equals(bnd.cvCreditos)) {
            Intent i = new Intent(this, CreditosActivity.class);
            i.putExtra("SOCIO", socio);
            startActivity(i);
            return;
        }

        // Ir a Recaudos
        if(v.equals(bnd.cvRecaudos)) {
            //TODO: Implementar redirección a la actividad de recaudos
            Toast.makeText(this, "Click en tarjeta de recaudos", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        final int DOCUMENTACION = 0;
        final int SIMULADOR_CREDITO = 1;
        final int SOLICITAR_CREDITO = 2;
        if(item.equals(bnd.bnvMenu.getMenu().getItem(DOCUMENTACION))) {
            //TODO: Implementar redirección a la actividad de documentación
            Toast.makeText(this, "Click documentación", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(item.equals(bnd.bnvMenu.getMenu().getItem(SIMULADOR_CREDITO))) {
            //TODO: Implementar redirección a la actividad del simulador de crédito
            Toast.makeText(this, "Click Simulador de crédito", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(item.equals(bnd.bnvMenu.getMenu().getItem(SOLICITAR_CREDITO))) {
            //TODO: Implementar redirección a la actividad de solicitud de crédito
            Toast.makeText(this, "Click solicitud de crédito", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }
}
