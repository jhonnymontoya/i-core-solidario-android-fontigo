package com.startlinesoft.icore.solidario.android.ais;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.Observer;
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

public class DashBoardActivity extends ICoreAppCompatActivity {

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
            this.bnd.progressBar.setVisibility(View.GONE);

            Ahorros ahorros = socio.getAhorros();
            Creditos creditos = socio.getCreditos();

            // Título de la entidad
            this.bnd.tbToolbar.setTitle(socio.getSiglaEntidad());

            this.bnd.ivImagen.setImageBitmap(ICoreGeneral.getSocioImagen(socio));

            this.bnd.tvTotalAhorros.setText(String.format("$%s", ahorros.getTotalAhorros()));
            this.bnd.pbPorcentajeIncremento.setProgress(ahorros.getPorcentajeIncremento());
            this.bnd.tvPorcentajeIncremento.setText(String.format("%s%%", ahorros.getPorcentajeIncremento()));

            if(creditos.getTotalSaldoCapital().contentEquals("0") == false || creditos.getCodeudas().size() > 0) {
                this.bnd.cvCreditos.setVisibility(View.VISIBLE);
                this.bnd.tvTotalCreditos.setText(String.format("$%s", creditos.getTotalSaldoCapital()));
                this.bnd.pbPorcentajeAbonado.setProgress(creditos.getPorcentajeAbonado());
                this.bnd.tvPorcentajeAbonado.setText(String.format("%s%%", creditos.getPorcentajeAbonado()));
            }

            if(socio.getRecaudo().size() > 0) {
                this.bnd.cvRecaudos.setVisibility(View.VISIBLE);
                Recaudo recaudo = socio.getRecaudo().get(0);
                this.bnd.tvTotalAplicado.setText(String.format("$%s", recaudo.getTotalAplicado()));
                this.bnd.tvFechaAplicacion.setText(ICoreGeneral.reverseFecha(recaudo.getFechaRecaudo()));
            }
            else {
                this.bnd.cvRecaudos.setVisibility(View.GONE);
                this.bnd.tvTotalAplicado.setText("$0");
                this.bnd.tvFechaAplicacion.setText("00-00-0000");
            }

            //TODO: Aqui lógica de guardar en preferencias usuario para el login

        });
    }
}