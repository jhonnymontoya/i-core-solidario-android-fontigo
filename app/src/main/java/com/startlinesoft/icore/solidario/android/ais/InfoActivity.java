package com.startlinesoft.icore.solidario.android.ais;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.startlinesoft.icore.solidario.ApiClient;
import com.startlinesoft.icore.solidario.ApiException;
import com.startlinesoft.icore.solidario.android.ais.databinding.ActivityInfoBinding;
import com.startlinesoft.icore.solidario.android.ais.enums.FuenteImagen;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreApiClient;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreGeneral;
import com.startlinesoft.icore.solidario.api.SocioApi;
import com.startlinesoft.icore.solidario.api.models.CambioImagen;
import com.startlinesoft.icore.solidario.api.models.CambioPasswordObject;
import com.startlinesoft.icore.solidario.api.models.Socio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Base64;

public class InfoActivity extends ICoreAppCompatActivity implements View.OnClickListener, CambiarImagenFragment.ItemClickListener {

    private ActivityInfoBinding bnd;
    private static final int CAMARA = 0;
    private static final int GALERIA = 1;

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
        this.bnd.tbToolbar.setNavigationIcon(R.drawable.ic_cerrar);
        this.bnd.tbToolbar.setNavigationOnClickListener(v -> this.finish());

        this.bnd.ivImagen.setImageBitmap(ICoreGeneral.getSocioImagen());
        this.bnd.tvNombre.setText(socio.getNombre());
        this.bnd.tvIdentificacion.setText(identificacion);
        this.bnd.tvNombreEntidad.setText(socio.getEntidad());

        this.bnd.btnPerfil.setOnClickListener(this);
        this.bnd.btnBeneficiarios.setOnClickListener(this);
        this.bnd.btnActualizarPassword.setOnClickListener(this);
        this.bnd.btnConfiguracion.setOnClickListener(this);
        this.bnd.btnSalir.setOnClickListener(this);
        this.bnd.btnAcercaDe.setOnClickListener(this);

        this.bnd.ivCambiarImagen.setOnClickListener(this);
        this.bnd.ivImagen.setOnClickListener(this);

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

        // Cambiar Imagen
        if (v.equals(bnd.ivCambiarImagen) || v.equals(this.bnd.ivImagen)) {
            CambiarImagenFragment cambiarImagenFragment = CambiarImagenFragment.newInstance();
            cambiarImagenFragment.show(this.getSupportFragmentManager(), CambiarImagenFragment.TAG);
        }
    }

    @Override
    public void onItemClick(FuenteImagen fuenteImagen) {
        if (fuenteImagen == FuenteImagen.CAMARA) {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            this.startActivityForResult(i, InfoActivity.CAMARA);
        }

        if (fuenteImagen == FuenteImagen.GALERIA) {
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            this.startActivityForResult(i, InfoActivity.GALERIA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != InfoActivity.RESULT_OK) return;

        Bitmap bitmap = null;

        switch (requestCode) {
            case InfoActivity.CAMARA:
                Bundle extras = data.getExtras();
                bitmap = (Bitmap) extras.get("data");
                break;
            case InfoActivity.GALERIA:
                Uri selectedImage = data.getData();
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.getContentResolver(), selectedImage));
                    } else {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

        if (bitmap == null) return;

        String base64 = this.bitmapToBase64(bitmap);

        this.bnd.progressBar.setVisibility(View.VISIBLE);

        this.verificarRed();

        ApiClient cliente = ICoreApiClient.getApiClient();
        SocioApi socioApi = new SocioApi(cliente);

        CambioImagen cambioImagen = new CambioImagen();
        cambioImagen.setImagen(base64);

        new Thread(() -> {
            try {
                socioApi.actualizarImagen(cambioImagen);
                this.bnd.progressBar.post(() -> {
                    this.bnd.progressBar.setVisibility(View.GONE);
                });
            } catch (ApiException e) {
            }
        }).start();
    }

    private String bitmapToBase64(Bitmap bitmap) {
        bitmap = this.escalarBitmap(bitmap);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        return Base64.getEncoder().encodeToString(byteArray);
    }

    private Bitmap escalarBitmap(Bitmap bitmap) {
        bitmap = this.normalizarBitmap(bitmap);

        Bitmap destino = null;
        if (bitmap.getWidth() >= bitmap.getHeight()) {
            destino = Bitmap.createBitmap(
                    bitmap,
                    bitmap.getWidth() / 2 - bitmap.getHeight() / 2,
                    0,
                    250,
                    250
            );
        } else {
            destino = Bitmap.createBitmap(
                    bitmap,
                    0,
                    bitmap.getHeight() / 2 - bitmap.getWidth() / 2,
                    250,
                    250
            );
        }

        return destino;
    }

    private Bitmap normalizarBitmap(Bitmap bitmap) {
        if (bitmap.getWidth() == 250 || bitmap.getHeight() == 250) return bitmap;

        int ancho = 0;
        int alto = 0;
        int diferencia = 0;
        float p = 0;

        if (bitmap.getWidth() < bitmap.getHeight()) {
            //menor ancho
            diferencia = 250 - bitmap.getWidth();
            ancho = 250;
            p = (diferencia * 100) / bitmap.getWidth();
            p = Math.abs(p);
            p = (bitmap.getHeight() > 250) ? 100 - p : p + 100;
            alto = (int) (p * bitmap.getHeight()) / 100;
        } else {
            //menor alto
            diferencia = 250 - bitmap.getHeight();
            alto = 250;
            p = (diferencia * 100) / bitmap.getHeight();
            p = Math.abs(p);
            p = (bitmap.getWidth() > 250) ? 100 - p : p + 100;
            ancho = (int) (p * bitmap.getWidth()) / 100;
        }

        return Bitmap.createScaledBitmap(bitmap, ancho, alto, false);
    }
}