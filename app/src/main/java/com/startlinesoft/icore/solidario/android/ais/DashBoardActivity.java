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
import com.startlinesoft.icore.solidario.api.LoginApi;
import com.startlinesoft.icore.solidario.api.SocioApi;
import com.startlinesoft.icore.solidario.api.models.Ahorros;
import com.startlinesoft.icore.solidario.api.models.Creditos;
import com.startlinesoft.icore.solidario.api.models.LoginInfo;
import com.startlinesoft.icore.solidario.api.models.Recaudo;
import com.startlinesoft.icore.solidario.api.models.Socio;

public class DashBoardActivity extends ICoreAppCompatActivity implements View.OnClickListener {

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

                bnd.ivImagen.setImageBitmap(bitmap);
                bnd.tvTotalAhorros.setText(String.format("$%s", ahorros.getTotalAhorros()));
                bnd.tvTotalCreditos.setText(String.format("$%s", creditos.getTotal()));
                bnd.pbPorcentajeAbonado.setProgress(creditos.getPorcentajeAbonado());
                bnd.tvPorcentajeAbonado.setText(String.format("%s%%", creditos.getPorcentajeAbonado()));

                if(socio.getRecaudo().size() > 0) {
                    Recaudo recaudo = socio.getRecaudo().get(0);
                    bnd.tvTotalAplicado.setText(String.format("$%s", recaudo.getTotalAplicado()));
                    bnd.tvFechaAplicacion.setText(recaudo.getFechaRecaudo());
                }
                else {
                    bnd.tvTotalAplicado.setText(String.format("$0"));
                    bnd.tvFechaAplicacion.setText(String.format("0000-00-00"));
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        //this.logout();
    }
}
