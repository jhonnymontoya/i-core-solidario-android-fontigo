package com.startlinesoft.icore.solidario.android.ais;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.startlinesoft.icore.solidario.android.ais.databinding.ActivityDashboardBinding;
import com.startlinesoft.icore.solidario.android.ais.models.SocioViewModel;
import com.startlinesoft.icore.solidario.android.ais.models.SocioViewModelFactory;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreGeneral;
import com.startlinesoft.icore.solidario.api.models.Ahorros;
import com.startlinesoft.icore.solidario.api.models.Creditos;
import com.startlinesoft.icore.solidario.api.models.Recaudo;
import com.startlinesoft.icore.solidario.api.models.Socio;

public class DashBoardActivity extends ICoreAppCompatActivity implements View.OnClickListener, MenuItem.OnMenuItemClickListener {

    private ActivityDashboardBinding bnd;
    private SocioViewModel socioViewModel;
    private Socio socio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bnd = ActivityDashboardBinding.inflate(this.getLayoutInflater());
        this.setContentView(this.bnd.getRoot());

        //Se valida token activo
        this.validarLogin();

        this.setSupportActionBar(this.bnd.tbToolbar);

        this.socioViewModel = new ViewModelProvider(this.getViewModelStore(), new SocioViewModelFactory())
                .get(SocioViewModel.class);

        this.bnd.progressBar.setVisibility(View.VISIBLE);
        socioViewModel.getSocio().observe(this, socio -> {
            this.socio = socio;
            ICoreGeneral.setSocio(socio);
            this.bnd.progressBar.setVisibility(View.GONE);

            Ahorros ahorros = socio.getAhorros();
            Creditos creditos = socio.getCreditos();

            // Título de la entidad
            this.bnd.tbToolbar.setTitle(socio.getSiglaEntidad());

            this.bnd.ivImagen.setImageBitmap(ICoreGeneral.getSocioImagen());

            this.bnd.tvTotalAhorros.setText(String.format("$%s", ahorros.getTotalAhorros()));
            this.bnd.pbPorcentajeIncremento.setProgress(ahorros.getPorcentajeIncremento());
            this.bnd.tvPorcentajeIncremento.setText(String.format("%s%%", ahorros.getPorcentajeIncremento()));

            if (creditos.getTotalSaldoCapital().contentEquals("0") == false || creditos.getCodeudas().size() > 0) {
                this.bnd.cvCreditos.setVisibility(View.VISIBLE);
                this.bnd.tvTotalCreditos.setText(String.format("$%s", creditos.getTotalSaldoCapital()));
                this.bnd.pbPorcentajeAbonado.setProgress(creditos.getPorcentajeAbonado());
                this.bnd.tvPorcentajeAbonado.setText(String.format("%s%%", creditos.getPorcentajeAbonado()));
            }

            if (socio.getRecaudo().size() > 0) {
                this.bnd.cvRecaudos.setVisibility(View.VISIBLE);
                Recaudo recaudo = socio.getRecaudo().get(0);
                this.bnd.tvTotalAplicado.setText(String.format("$%s", recaudo.getTotalAplicado()));
                this.bnd.tvFechaAplicacion.setText(ICoreGeneral.reverseFecha(recaudo.getFechaRecaudo()));
            } else {
                this.bnd.cvRecaudos.setVisibility(View.GONE);
                this.bnd.tvTotalAplicado.setText("$0");
                this.bnd.tvFechaAplicacion.setText("00-00-0000");
            }

            //TODO: Aqui lógica de guardar en preferencias usuario para el login

        });

        this.bnd.ivImagen.setOnClickListener(this);

        this.bnd.cvAhorros.setOnClickListener(this);
        this.bnd.cvCreditos.setOnClickListener(this);
        this.bnd.cvRecaudos.setOnClickListener(this);

        // Barra de navegación inferior
        this.bnd.bnvMenu.getMenu().setGroupCheckable(0, true, false);
        for (int i = 0; i < this.bnd.bnvMenu.getMenu().size(); i++) {
            this.bnd.bnvMenu.getMenu().getItem(i).setChecked(true);
            this.bnd.bnvMenu.getMenu().getItem(i).setOnMenuItemClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (this.isVibradorActivado()) {
            this.vibrar();
        }
        final int DOCUMENTACION = 0;
        final int SIMULADOR_CREDITO = 1;
        final int SOLICITAR_CREDITO = 2;
        if (item.equals(bnd.bnvMenu.getMenu().getItem(DOCUMENTACION))) {
            //TODO: Implementar redirección a la actividad de documentación
            Toast.makeText(this, "Click documentación", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (item.equals(bnd.bnvMenu.getMenu().getItem(SIMULADOR_CREDITO))) {
            //TODO: Implementar redirección a la actividad del simulador de crédito
            Toast.makeText(this, "Click Simulador de crédito", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (item.equals(bnd.bnvMenu.getMenu().getItem(SOLICITAR_CREDITO))) {
            //TODO: Implementar redirección a la actividad de solicitud de crédito
            Toast.makeText(this, "Click solicitud de crédito", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }
}