package com.startlinesoft.icore.solidario.android.ais;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.startlinesoft.icore.solidario.android.ais.databinding.ActivityInfoBinding;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreGeneral;
import com.startlinesoft.icore.solidario.api.models.Socio;

public class InfoActivity extends ICoreAppCompatActivity implements View.OnClickListener {

    private ActivityInfoBinding bnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bnd = ActivityInfoBinding.inflate(this.getLayoutInflater());
        setContentView(this.bnd.getRoot());

        //Se valida token activo
        this.validarLogin();

        Socio socio = ICoreGeneral.getSocio();
        String identificacion = String.format("%s %s", socio.getTipoIdentificacion(), socio.getIdentificacion());

        setSupportActionBar(bnd.tbToolbar);
        bnd.tbToolbar.setNavigationIcon(R.drawable.ic_cerrar);
        bnd.tbToolbar.setNavigationOnClickListener(v -> this.finish());

        bnd.ivImagen.setImageBitmap(ICoreGeneral.getSocioImagen());
        bnd.tvNombre.setText(socio.getNombre());
        bnd.tvIdentificacion.setText(identificacion);
        bnd.tvNombreEntidad.setText(socio.getEntidad());

        bnd.btnPerfil.setOnClickListener(this);
        bnd.btnBeneficiarios.setOnClickListener(this);
        bnd.btnActualizarPassword.setOnClickListener(this);
        bnd.btnConfiguracion.setOnClickListener(this);
        bnd.btnSalir.setOnClickListener(this);
        bnd.btnAcercaDe.setOnClickListener(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        removerTituloBarra();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        // Perfil
        if (v.equals(bnd.btnPerfil)) {
            Intent i = new Intent(this, PerfilActivity.class);
            this.startActivity(i);
            return;
        }

        // Beneficiarios
        if (v.equals(bnd.btnBeneficiarios)) {
            Intent i = new Intent(this, BeneficiariosActivity.class);
            this.startActivity(i);
            return;
        }

        // Actualizar contraseña
        if (v.equals(bnd.btnActualizarPassword)) {
            Intent i = new Intent(this, CambiarPasswordActivity.class);
            this.startActivity(i);
            return;
        }

        // Configuración
        if (v.equals(bnd.btnConfiguracion)) {
            Intent i = new Intent(this, ConfiguracionActivity.class);
            this.startActivity(i);
            return;
        }

        // Salir
        if (v.equals(bnd.btnSalir)) {
            this.bnd.progressBar.setVisibility(View.VISIBLE);
            logout();
            return;
        }

        // Acerca de
        if (v.equals(bnd.btnAcercaDe)) {
            Intent i = new Intent(this, AboutActivity.class);
            this.startActivity(i);
        }
    }
}