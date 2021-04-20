package com.startlinesoft.icore.solidario.android.fontigo;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.startlinesoft.icore.solidario.android.fontigo.databinding.ActivityPerfilBinding;
import com.startlinesoft.icore.solidario.android.fontigo.models.PerfilViewModel;
import com.startlinesoft.icore.solidario.android.fontigo.models.PerfilViewModelFactory;
import com.startlinesoft.icore.solidario.android.fontigo.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.android.fontigo.utilidades.ICoreGeneral;
import com.startlinesoft.icore.solidario.api.models.Signo;
import com.startlinesoft.icore.solidario.api.models.Socio;

public class PerfilActivity extends ICoreAppCompatActivity {

    private ActivityPerfilBinding bnd;
    private PerfilViewModel perfilViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bnd = ActivityPerfilBinding.inflate(this.getLayoutInflater());
        this.setContentView(this.bnd.getRoot());

        //Se valida token activo
        this.validarLogin();

        this.perfilViewModel = new ViewModelProvider(this.getViewModelStore(), new PerfilViewModelFactory())
                .get(PerfilViewModel.class);

        this.bnd.progressBar.setVisibility(View.VISIBLE);
        this.perfilViewModel.getPerfil().observe(this, perfil -> {
            this.bnd.progressBar.setVisibility(View.GONE);

            String fechaPatron = "%s (%s)";

            String fechaNacimeinto = ICoreGeneral.reverseFecha(perfil.getFechaNacimiento().toString());
            fechaNacimeinto = String.format(fechaPatron, fechaNacimeinto, perfil.getEdad());
            String fechaIngreso = ICoreGeneral.reverseFecha(perfil.getFechaIngresoEmpresa().toString());
            fechaIngreso = String.format(fechaPatron, fechaIngreso, perfil.getAntiguedadEmpresa());
            String fechaAfiliacion = ICoreGeneral.reverseFecha(perfil.getFechaAfiliacion().toString());
            fechaAfiliacion = String.format(fechaPatron, fechaAfiliacion, perfil.getAntiguedadFondo());

            this.bnd.tvEmpresa.setText(perfil.getEmpresa());
            this.bnd.tvFechaNacimiento.setText(fechaNacimeinto);
            this.bnd.tvFechaIngresoEmpresa.setText(fechaIngreso);
            this.bnd.tvFechaAfiliacion.setText(fechaAfiliacion);
            this.bnd.tvEmail.setText(perfil.getEmail());
            this.bnd.tvTelefono.setText(perfil.getTelefono());
            if (perfil.getSignoCupoDisponible() == Signo.POSITIVO) {
                this.bnd.tvCupo.setTextColor(this.getColor(R.color.azul));
            } else {
                this.bnd.tvCupo.setTextColor(this.getColor(R.color.rojo));
            }
            this.bnd.tvCupo.setText(String.format("$%s", perfil.getCupoDisponible()));
            if (perfil.getSignoEndeudamiento() == Signo.POSITIVO) {
                this.bnd.tvEndeudamiento.setTextColor(this.getColor(R.color.azul));
            } else {
                this.bnd.tvEndeudamiento.setTextColor(this.getColor(R.color.rojo));
            }
            this.bnd.tvEndeudamiento.setText(String.format("%s%%", perfil.getEndeudamiento()));
        });

        Socio socio = ICoreGeneral.getSocio();
        String identificacion = String.format("%s %s", socio.getTipoIdentificacion(), socio.getIdentificacion());

        setSupportActionBar(bnd.tbToolbar);
        this.getSupportActionBar().setTitle(R.string.perfil);
        bnd.tbToolbar.setNavigationIcon(R.drawable.ic_cerrar);
        bnd.tbToolbar.setNavigationOnClickListener(v -> this.finish());

        bnd.ivImagen.setImageBitmap(ICoreGeneral.getSocioImagen());
        bnd.tvNombre.setText(socio.getNombre());
        bnd.tvIdentificacion.setText(identificacion);
        bnd.tvNombreEntidad.setText(socio.getEntidad());
    }
}