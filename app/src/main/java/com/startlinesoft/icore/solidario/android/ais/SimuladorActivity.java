package com.startlinesoft.icore.solidario.android.ais;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;


import androidx.annotation.Nullable;

import com.startlinesoft.icore.solidario.ApiClient;
import com.startlinesoft.icore.solidario.ApiException;
import com.startlinesoft.icore.solidario.android.ais.databinding.ActivitySimuladorBinding;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreApiClient;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreGeneral;
import com.startlinesoft.icore.solidario.api.CreditosApi;
import com.startlinesoft.icore.solidario.api.models.ModalidadCredito;

import java.util.List;

public class SimuladorActivity extends ICoreAppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ActivitySimuladorBinding bnd;
    private CreditosApi creditosApi;
    private List<ModalidadCredito> modalidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bnd = ActivitySimuladorBinding.inflate(this.getLayoutInflater());
        this.setContentView(this.bnd.getRoot());

        //Se valida token activo
        this.validarLogin();

        this.setSupportActionBar(this.bnd.tbToolbar);
        this.bnd.tbToolbar.setNavigationIcon(R.drawable.ic_angle_left);
        this.bnd.tbToolbar.setNavigationOnClickListener(v -> this.finish());

        this.bnd.ivImagen.setImageBitmap(ICoreGeneral.getSocioImagen());
        this.bnd.ivImagen.setOnClickListener(this);

        this.bnd.progressBar.setVisibility(View.VISIBLE);

        ApiClient cliente = ICoreApiClient.getApiClient();
        this.creditosApi = new CreditosApi(cliente);

        this.bnd.modalidadCredito.setOnItemClickListener(this);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.bnd.tbToolbar.setTitle(getString(R.string.accion_simulador));

        new Thread(() -> {
            try {
                this.modalidades = this.creditosApi.obtenerModalidadesDisponibles();
                this.bnd.progressBar.post(() -> {
                    this.bnd.progressBar.setVisibility(View.GONE);

                    if(this.modalidades.size() > 0){
                        String[] modalidades = this.modalidades
                                .stream()
                                .map(m -> m.getNombre())
                                .toArray(String[]::new);

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, modalidades);
                        this.bnd.modalidadCredito.setAdapter(adapter);
                        this.bnd.modalidadCredito.setText(adapter.getItem(0), false);

                        this.cargarPeriodicidades(this.modalidades.get(0));
                    }
                });
            } catch (ApiException ignored) {
            }
        }).start();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.cargarPeriodicidades(this.modalidades.get(position));
        String item = parent.getItemAtPosition(position).toString();
        Toast.makeText(this, item + " ; " + this.modalidades.get(position).getNombre(), Toast.LENGTH_SHORT).show();
    }

    private void cargarPeriodicidades(ModalidadCredito modalidadCredito){
        String[] periodicidades = modalidadCredito.getPeriodicidadesDePago()
                .stream()
                .map(p -> p.toString())
                .toArray(String[]::new);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, periodicidades);
        this.bnd.periodicidad.setAdapter(adapter);
        this.bnd.periodicidad.setText(adapter.getItem(0), false);
    }
}