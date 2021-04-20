package com.startlinesoft.icore.solidario.android.fontigo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.startlinesoft.icore.solidario.android.fontigo.databinding.ActivityDashboardBinding;
import com.startlinesoft.icore.solidario.android.fontigo.models.SocioViewModel;
import com.startlinesoft.icore.solidario.android.fontigo.models.SocioViewModelFactory;
import com.startlinesoft.icore.solidario.android.fontigo.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.android.fontigo.utilidades.ICoreGeneral;
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
    protected void onPostResume() {
        super.onPostResume();
        this.socioViewModel.getSocio().observe(this, socio -> {
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

            this.guardarDatosDeUsuarioLogin();
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.guardarDatosDeUsuarioLogin();
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

        // Ir a Ahorros
        if (v.equals(bnd.cvAhorros)) {
            Intent i = new Intent(this, AhorrosActivity.class);
            startActivity(i);
            return;
        }

        // Ir a Créditos
        if (v.equals(bnd.cvCreditos)) {
            Intent i = new Intent(this, CreditosActivity.class);
            startActivity(i);
            return;
        }

        // Ir a Recaudos
        if (v.equals(bnd.cvRecaudos)) {
            Intent i = new Intent(this, RecaudosActivity.class);
            startActivity(i);
            return;
        }
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
            Intent i = new Intent(this, DocumentacionActivity.class);
            this.startActivity(i);
            return false;
        }
        if (item.equals(bnd.bnvMenu.getMenu().getItem(SIMULADOR_CREDITO))) {
            Intent i = new Intent(this, SimuladorActivity.class);
            this.startActivity(i);
            return false;
        }
        if (item.equals(bnd.bnvMenu.getMenu().getItem(SOLICITAR_CREDITO))) {
            Intent i = new Intent(this, SolicitarCreditoActivity.class);
            this.startActivity(i);
            return false;
        }
        return false;
    }
}