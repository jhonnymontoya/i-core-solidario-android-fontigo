package com.startlinesoft.icore.solidario.android.fontigo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.startlinesoft.icore.solidario.android.fontigo.adapters.adapters.BeneficiarioAdapter;
import com.startlinesoft.icore.solidario.android.fontigo.databinding.ActivityBeneficiariosBinding;
import com.startlinesoft.icore.solidario.android.fontigo.models.BeneficiarioViewModel;
import com.startlinesoft.icore.solidario.android.fontigo.models.BeneficiarioViewModelFactory;
import com.startlinesoft.icore.solidario.android.fontigo.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.android.fontigo.utilidades.ICoreGeneral;

public class BeneficiariosActivity extends ICoreAppCompatActivity implements View.OnClickListener {

    private ActivityBeneficiariosBinding bnd;
    private BeneficiarioViewModel beneficiarioViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bnd = ActivityBeneficiariosBinding.inflate(this.getLayoutInflater());
        this.setContentView(this.bnd.getRoot());

        //Se valida token activo
        this.validarLogin();

        this.beneficiarioViewModel = new ViewModelProvider(this.getViewModelStore(), new BeneficiarioViewModelFactory())
                .get(BeneficiarioViewModel.class);

        this.bnd.progressBar.setVisibility(View.VISIBLE);
        this.beneficiarioViewModel.getBeneficiarios().observe(this, beneficiarios -> {
            this.bnd.progressBar.setVisibility(View.GONE);

            if(beneficiarios.size() > 0){
                String cantidad = "%s %s";
                if(beneficiarios.size() == 1) {
                    cantidad = String.format(
                            cantidad,
                            beneficiarios.size(),
                            this.getString(R.string.beneficiarios_singular)
                    ).toLowerCase();
                }
                else{
                    cantidad = String.format(
                            cantidad,
                            beneficiarios.size(),
                            this.getString(R.string.beneficiarios)
                    ).toLowerCase();
                }
                this.bnd.tvCantidad.setText(cantidad);
                this.bnd.rvBeneficiarios.setLayoutManager(new LinearLayoutManager(this));
                BeneficiarioAdapter ba = new BeneficiarioAdapter(beneficiarios);
                this.bnd.rvBeneficiarios.setAdapter(ba);
            }
            else{
                this.bnd.tvBeneficiariosSinRegistros.setVisibility(View.VISIBLE);
            }
        });

        this.setSupportActionBar(this.bnd.tbToolbar);
        this.bnd.tbToolbar.setNavigationIcon(R.drawable.ic_angle_left);
        this.bnd.tbToolbar.setNavigationOnClickListener(v -> this.finish());

        this.bnd.ivImagen.setImageBitmap(ICoreGeneral.getSocioImagen());
        this.bnd.ivImagen.setOnClickListener(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.bnd.tbToolbar.setTitle(getString(R.string.beneficiarios));
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
    }
}