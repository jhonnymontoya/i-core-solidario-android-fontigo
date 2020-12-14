package com.startlinesoft.icore.solidario.android.ais;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.startlinesoft.icore.solidario.android.ais.databinding.ActivityPerfilBinding;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreGeneral;
import com.startlinesoft.icore.solidario.api.models.Socio;

public class PerfilActivity extends ICoreAppCompatActivity {

    private ActivityPerfilBinding bnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bnd = ActivityPerfilBinding.inflate(this.getLayoutInflater());
        this.setContentView(this.bnd.getRoot());

        //Se valida token activo
        this.validarLogin();

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