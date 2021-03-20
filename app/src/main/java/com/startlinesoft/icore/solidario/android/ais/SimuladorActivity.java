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
import com.startlinesoft.icore.solidario.api.models.CreditoSimulado;
import com.startlinesoft.icore.solidario.api.models.ModalidadCredito;
import com.startlinesoft.icore.solidario.api.models.Periodicidad;
import com.startlinesoft.icore.solidario.api.models.SolicitudCreditoSimulado;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SimuladorActivity extends ICoreAppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ActivitySimuladorBinding bnd;
    private CreditosApi creditosApi;
    private List<ModalidadCredito> modalidades;
    private int idModalidadCreditoSeleccionada = 0;

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
        this.bnd.btnSimular.setOnClickListener(this);
        this.bnd.btnVolverSimular.setOnClickListener(this);

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

                    if (this.modalidades.size() > 0) {
                        String[] modalidades = this.modalidades
                                .stream()
                                .map(m -> m.getNombre())
                                .toArray(String[]::new);

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, modalidades);
                        this.bnd.modalidadCredito.setAdapter(adapter);
                        this.bnd.modalidadCredito.setText(adapter.getItem(0), false);

                        this.cargarPeriodicidades(this.modalidades.get(0));
                        this.idModalidadCreditoSeleccionada = this.modalidades.get(0).getId();
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

        if (v.equals(bnd.btnVolverSimular)) {
            this.bnd.contenedorSimulador.setVisibility(View.VISIBLE);
            this.bnd.btnSimular.setVisibility(View.VISIBLE);
            this.bnd.btnVolverSimular.setVisibility(View.GONE);
            this.bnd.contenedorSimuladorResultados.setVisibility(View.GONE);
            return;
        }

        if (v.equals(bnd.btnSimular)) {
            this.bnd.progressBar.setVisibility(View.VISIBLE);

            this.verificarRed();

            Periodicidad periodicidad = Periodicidad.fromValue(this.bnd.periodicidad.getText().toString());
            Integer valor = Integer.parseInt(this.bnd.valorCredito.getText().toString());
            Integer plazo = Integer.parseInt(this.bnd.plazo.getText().toString());

            SolicitudCreditoSimulado solicitudCreditoSimulado = new SolicitudCreditoSimulado();
            solicitudCreditoSimulado.setModalidadCreditoId(this.idModalidadCreditoSeleccionada);
            solicitudCreditoSimulado.setPeriodicidadDePago(periodicidad);
            solicitudCreditoSimulado.setValorCredito(valor);
            solicitudCreditoSimulado.setPlazo(plazo);

            this.bnd.tvError.setText("");

            new Thread(() -> {
                try {
                    CreditoSimulado creditoSimulado = this.creditosApi.simularCredito(solicitudCreditoSimulado);
                    this.bnd.progressBar.post(() -> {
                        String fechaSimulacion = ICoreGeneral.reverseFecha(creditoSimulado.getFechaSimulacion().toString());
                        String fechaPrimerPago = ICoreGeneral.reverseFecha(creditoSimulado.getFechaPrimerPago().toString());
                        String fechaUltimoPago = ICoreGeneral.reverseFecha(creditoSimulado.getFechaUltimoPago().toString());

                        this.bnd.tvFechaSimulacion.setText(fechaSimulacion);
                        this.bnd.tvmodalidadCredito.setText(creditoSimulado.getModalidadDeCredito());
                        this.bnd.tvPeriodicidad.setText(creditoSimulado.getPeriodicidad().toString());
                        this.bnd.tvValorCredito.setText(creditoSimulado.getValorCredito());
                        this.bnd.tvValorCredito.setTextColor(this.getColor(R.color.azul));
                        this.bnd.tvPlazo.setText(creditoSimulado.getPlazo());
                        this.bnd.tvTasa.setText(creditoSimulado.getTasa());
                        this.bnd.tvFechaPrimerPago.setText(fechaPrimerPago);
                        this.bnd.tvFechaUltimoPago.setText(fechaUltimoPago);
                        this.bnd.tvValorCuota.setText(creditoSimulado.getValorCuota());
                        this.bnd.tvValorCuota.setTextColor(this.getColor(R.color.azul));

                        this.bnd.contenedorSimulador.setVisibility(View.GONE);
                        this.bnd.btnSimular.setVisibility(View.GONE);
                        this.bnd.btnVolverSimular.setVisibility(View.VISIBLE);
                        this.bnd.contenedorSimuladorResultados.setVisibility(View.VISIBLE);
                        this.bnd.progressBar.setVisibility(View.GONE);

                    });
                } catch (ApiException e) {
                    bnd.tvError.post(() -> {
                        String msg;
                        switch (e.getCode()) {
                            case 400: //Entradas no válidas
                                try {
                                    msg = new JSONObject(e.getResponseBody()).getString("message");
                                } catch (JSONException je) {
                                    msg = "Error: Intentelo más tarde";
                                }
                                break;
                            case 422: //Entradas no procesables
                            case 429: //Demasiados intentos
                                msg = "Datos no válidos";
                                break;
                            case 426: //Se requiere actualización
                                msg = "Se requiere actualización";
                                break;
                            case 401:
                                msg = "Usuario o contraseña no válidos";
                                break;
                            case 412:
                                try {
                                    msg = new JSONObject(e.getResponseBody()).getString("message");
                                } catch (JSONException je) {
                                    msg = "App Movil no activa";
                                }
                                break;
                            default:
                                msg = "Entradas no válidos";
                                break;
                        }
                        this.bnd.tvError.setText(msg);
                        this.bnd.progressBar.setVisibility(View.GONE);
                    });
                } catch (Exception e) {
                }
            }).start();

            return;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.cargarPeriodicidades(this.modalidades.get(position));
        this.idModalidadCreditoSeleccionada = this.modalidades.get(position).getId();
    }

    private void cargarPeriodicidades(ModalidadCredito modalidadCredito) {
        String[] periodicidades = modalidadCredito.getPeriodicidadesDePago()
                .stream()
                .map(p -> p.toString())
                .toArray(String[]::new);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, periodicidades);
        this.bnd.periodicidad.setAdapter(adapter);
        this.bnd.periodicidad.setText(adapter.getItem(0), false);
    }
}