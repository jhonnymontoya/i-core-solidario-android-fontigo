package com.startlinesoft.icore.solidario.android.ais;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.startlinesoft.icore.solidario.android.ais.databinding.ActivityDashboardBinding;
import com.startlinesoft.icore.solidario.android.ais.models.SocioViewModel;
import com.startlinesoft.icore.solidario.android.ais.models.SocioViewModelFactory;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.api.models.Ahorros;
import com.startlinesoft.icore.solidario.api.models.Creditos;
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
        });
    }
}