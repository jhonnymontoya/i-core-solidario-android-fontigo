package com.startlinesoft.icore.solidario.android.fontigo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

import com.startlinesoft.icore.solidario.ApiClient;
import com.startlinesoft.icore.solidario.ApiException;
import com.startlinesoft.icore.solidario.android.fontigo.databinding.ActivityDocumentacionBinding;
import com.startlinesoft.icore.solidario.android.fontigo.utilidades.ICoreApiClient;
import com.startlinesoft.icore.solidario.android.fontigo.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.android.fontigo.utilidades.ICoreGeneral;
import com.startlinesoft.icore.solidario.api.DocumentacionApi;
import com.startlinesoft.icore.solidario.api.models.RespuestaGeneral;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DocumentacionActivity extends ICoreAppCompatActivity implements View.OnClickListener {

    private ActivityDocumentacionBinding bnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bnd = ActivityDocumentacionBinding.inflate(this.getLayoutInflater());
        this.setContentView(this.bnd.getRoot());

        //Se valida token activo
        this.validarLogin();

        this.setSupportActionBar(this.bnd.tbToolbar);
        this.bnd.tbToolbar.setNavigationIcon(R.drawable.ic_angle_left);
        this.bnd.tbToolbar.setNavigationOnClickListener(v -> this.finish());

        this.bnd.ivImagen.setImageBitmap(ICoreGeneral.getSocioImagen());
        this.bnd.ivImagen.setOnClickListener(this);

        List<String> anios = new ArrayList<String>();
        for (int anio = Calendar.getInstance().get(Calendar.YEAR) - 1; anio >= 2018; anio--) {
            anios.add(String.valueOf(anio));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, anios);
        this.bnd.spCertificadoTributarioAnios.setAdapter(adapter);

        this.bnd.btnSolicitarCertificadoTributario.setOnClickListener(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.bnd.tbToolbar.setTitle(getString(R.string.accion_documentacion));
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

        if (v.equals(this.bnd.btnSolicitarCertificadoTributario)) {
            this.bnd.progressBar.setVisibility(View.VISIBLE);

            this.verificarRed();

            ApiClient cliente = ICoreApiClient.getApiClient();
            DocumentacionApi documentacionApi = new DocumentacionApi(cliente);

            Integer anio = Integer.parseInt(
                    this.bnd.spCertificadoTributarioAnios.getSelectedItem().toString()
            );

            this.bnd.tvMensaje.setText("");
            this.bnd.tvError.setText("");

            new Thread(() -> {
                try {
                    RespuestaGeneral respuestaGeneral = documentacionApi.documentacionCertificadoTributario(anio);
                    this.bnd.progressBar.post(() -> {
                        this.bnd.progressBar.setVisibility(View.GONE);
                        this.bnd.tvMensaje.setText(respuestaGeneral.getRespuesta());
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
                }
            }).start();
        }
    }
}