package com.startlinesoft.icore.solidario.android.ais;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.startlinesoft.icore.solidario.android.ais.adapters.CodeudaAdapter;
import com.startlinesoft.icore.solidario.android.ais.adapters.CreditoAdapter;
import com.startlinesoft.icore.solidario.android.ais.databinding.ActivityCreditosBinding;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.api.models.Codeuda;
import com.startlinesoft.icore.solidario.api.models.Credito;
import com.startlinesoft.icore.solidario.api.models.Creditos;
import com.startlinesoft.icore.solidario.api.models.Socio;

import java.util.List;

public class CreditosActivity extends ICoreAppCompatActivity implements View.OnClickListener {

    private ActivityCreditosBinding bnd;
    private Socio socio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bnd = ActivityCreditosBinding.inflate(getLayoutInflater());
        setContentView(bnd.getRoot());

        //Se valida token activo
        this.validarLogin();

        socio = (Socio) getIntent().getSerializableExtra("SOCIO");

        setSupportActionBar(bnd.tbToolbar);
        bnd.tbToolbar.setNavigationIcon(R.drawable.ic_angle_left);
        bnd.tbToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bitmap bitmap = BitmapFactory.decodeByteArray(
                socio.getImagen(),
                0,
                socio.getImagen().length
        );
        bnd.ivImagen.setImageBitmap(bitmap);
        bnd.ivImagen.setOnClickListener(this);

        Creditos listaCreditos = socio.getCreditos();

        //Créditos
        bnd.rvCreditos.setLayoutManager(new LinearLayoutManager(this));
        List<Credito> creditos = listaCreditos.getCreditos();
        bnd.rvCreditos.setAdapter(new CreditoAdapter(creditos));

        //Codeudas
        if(listaCreditos.getCodeudas().size() > 0) {
            bnd.rvCodeudas.setLayoutManager(new LinearLayoutManager(this));
            List<Codeuda> codeudas = listaCreditos.getCodeudas();
            bnd.rvCodeudas.setAdapter(new CodeudaAdapter(codeudas));
            bnd.cvCodeudas.setVisibility(View.VISIBLE);
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

        // Ir a info de cuenta
        if(v.equals(bnd.ivImagen)) {
            Intent i = new Intent(this, InfoActivity.class);
            i.putExtra("SOCIO", socio);
            startActivity(i);
            return;
        }

        //TODO: Implementar acciones aqui

    }
}
