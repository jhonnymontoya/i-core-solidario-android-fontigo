package com.startlinesoft.icore.solidario.android.ais;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.startlinesoft.icore.solidario.ApiClient;
import com.startlinesoft.icore.solidario.ApiException;
import com.startlinesoft.icore.solidario.android.ais.databinding.ActivitySolicitarCreditoBinding;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreApiClient;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreGeneral;
import com.startlinesoft.icore.solidario.api.CreditosApi;
import com.startlinesoft.icore.solidario.api.models.CreditoSimulado;
import com.startlinesoft.icore.solidario.api.models.ModalidadCredito;
import com.startlinesoft.icore.solidario.api.models.Periodicidad;
import com.startlinesoft.icore.solidario.api.models.RespuestaGeneral;
import com.startlinesoft.icore.solidario.api.models.SolicitudCredito;
import com.startlinesoft.icore.solidario.api.models.SolicitudCreditoSimulado;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SolicitarCreditoActivity extends ICoreAppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ActivitySolicitarCreditoBinding bnd;
    private CreditosApi creditosApi;
    private List<ModalidadCredito> modalidades;
    private int idModalidadCreditoSeleccionada = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bnd = ActivitySolicitarCreditoBinding.inflate(this.getLayoutInflater());
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
        this.bnd.btnSolicitarCredito.setOnClickListener(this);
        this.bnd.btnVolverSolicitarCredito.setOnClickListener(this);
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

        if (v.equals(bnd.btnVolverSolicitarCredito)) {
            this.bnd.contenedorSolicitarCredito.setVisibility(View.VISIBLE);
            this.bnd.btnSolicitarCredito.setVisibility(View.VISIBLE);
            this.bnd.btnVolverSolicitarCredito.setVisibility(View.GONE);
            this.bnd.contenedorSolicitarCreditoResultadosResultados.setVisibility(View.GONE);
            return;
        }

        if (v.equals(bnd.btnSolicitarCredito)) {
            this.bnd.progressBar.setVisibility(View.VISIBLE);

            this.verificarRed();

            Integer valor = Integer.parseInt(this.bnd.valorCredito.getText().toString());
            Integer plazo = Integer.parseInt(this.bnd.plazo.getText().toString());

            SolicitudCredito solicitudCredito = new SolicitudCredito();
            solicitudCredito.setModalidadCreditoId(this.idModalidadCreditoSeleccionada);
            solicitudCredito.setValorCredito(valor);
            solicitudCredito.setPlazo(plazo);
            solicitudCredito.setObservaciones(this.bnd.observaciones.getText().toString());

            this.bnd.tvError.setText("");

            new Thread(() -> {
                try {
                    RespuestaGeneral respuesta = this.creditosApi.solicitarCredito(solicitudCredito);
                    this.bnd.progressBar.post(() -> {

                        this.bnd.mensajeResultado.setText(respuesta.getRespuesta());

                        this.bnd.contenedorSolicitarCredito.setVisibility(View.GONE);
                        this.bnd.btnSolicitarCredito.setVisibility(View.GONE);
                        this.bnd.btnVolverSolicitarCredito.setVisibility(View.VISIBLE);
                        this.bnd.contenedorSolicitarCreditoResultadosResultados.setVisibility(View.VISIBLE);
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
        this.idModalidadCreditoSeleccionada = this.modalidades.get(position).getId();
    }
}