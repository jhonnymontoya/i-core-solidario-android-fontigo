package com.startlinesoft.icore.solidario.android.ais;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.startlinesoft.icore.solidario.ApiClient;
import com.startlinesoft.icore.solidario.ApiException;
import com.startlinesoft.icore.solidario.android.ais.databinding.ActivityDashBoardBinding;
import com.startlinesoft.icore.solidario.android.ais.models.SocioViewModel;
import com.startlinesoft.icore.solidario.android.ais.models.SocioViewModelFactory;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreApiClient;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreGeneral;
import com.startlinesoft.icore.solidario.api.LoginApi;
import com.startlinesoft.icore.solidario.api.SocioApi;
import com.startlinesoft.icore.solidario.api.models.Ahorros;
import com.startlinesoft.icore.solidario.api.models.Creditos;
import com.startlinesoft.icore.solidario.api.models.LoginInfo;
import com.startlinesoft.icore.solidario.api.models.Recaudo;
import com.startlinesoft.icore.solidario.api.models.Socio;

public class DashBoardActivity extends ICoreAppCompatActivity implements View.OnClickListener, MenuItem.OnMenuItemClickListener {

    private ActivityDashBoardBinding bnd;
    private SocioViewModel socioViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bnd = ActivityDashBoardBinding.inflate(getLayoutInflater());
        setContentView(bnd.getRoot());

        //Se valida token activo
        this.validarLogin();

        setSupportActionBar(bnd.tbToolbar);

        //tb.setNavigationIcon();

        socioViewModel = new ViewModelProvider(getViewModelStore(), new SocioViewModelFactory())
                .get(SocioViewModel.class);

        bnd.progressBar.setVisibility(View.VISIBLE);
        socioViewModel.getSocio().observe(this, new Observer<Socio>() {
            @Override
            public void onChanged(Socio socio) {
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

                bnd.tvTotalCreditos.setText(String.format("$%s", creditos.getTotalSaldoCapital()));
                bnd.pbPorcentajeAbonado.setProgress(creditos.getPorcentajeAbonado());
                bnd.tvPorcentajeAbonado.setText(String.format("%s%%", creditos.getPorcentajeAbonado()));

                if(socio.getRecaudo().size() > 0) {
                    Recaudo recaudo = socio.getRecaudo().get(0);
                    bnd.tvTotalAplicado.setText(String.format("$%s", recaudo.getTotalAplicado()));
                    bnd.tvFechaAplicacion.setText(
                            ICoreGeneral.reverseFecha(recaudo.getFechaRecaudo())
                    );
                }
                else {
                    bnd.tvTotalAplicado.setText(String.format("$0"));
                    bnd.tvFechaAplicacion.setText(String.format("00-00-0000"));
                }

            }
        });

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
        // Ir a Ahorros
        if(v.equals(bnd.cvAhorros)) {
            Toast.makeText(this, "Click en tarjeta de ahorros", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ir a Créditos
        if(v.equals(bnd.cvCreditos)) {
            Toast.makeText(this, "Click en tarjeta de créditos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ir a Recaudos
        if(v.equals(bnd.cvRecaudos)) {
            Toast.makeText(this, "Click en tarjeta de recaudos", Toast.LENGTH_SHORT).show();
            return;
        }
        //this.logout();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        final int DOCUMENTACION = 0;
        final int SIMULADOR_CREDITO = 1;
        final int SOLICITAR_CREDITO = 2;
        if(item.equals(bnd.bnvMenu.getMenu().getItem(DOCUMENTACION))) {
            Toast.makeText(this, "Click documentación", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(item.equals(bnd.bnvMenu.getMenu().getItem(SIMULADOR_CREDITO))) {
            Toast.makeText(this, "Click Simulador de crédito", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(item.equals(bnd.bnvMenu.getMenu().getItem(SOLICITAR_CREDITO))) {
            Toast.makeText(this, "Click solicitud de crédito", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }
}
