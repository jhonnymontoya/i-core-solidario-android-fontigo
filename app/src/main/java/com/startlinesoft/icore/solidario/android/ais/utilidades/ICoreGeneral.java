package com.startlinesoft.icore.solidario.android.ais.utilidades;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.startlinesoft.icore.solidario.api.models.Socio;

public class ICoreGeneral {

    /**
     * Retorna una fecha de formato yyyy-mm-dd a dd-mm-yyyy
     *
     * @param fecha
     * @return fecha
     */
    public static String reverseFecha(String fecha) {
        String[] tmp = fecha.split("-");
        if (tmp.length != 3) {
            return fecha;
        }
        String fechaFormateada = "%s-%s-%s";
        fechaFormateada = String.format(fechaFormateada, tmp[2], tmp[1], tmp[0]);
        return fechaFormateada;
    }

    public static Bitmap getSocioImagen(Socio socio) {
        return BitmapFactory.decodeByteArray(socio.getImagen(), 0, socio.getImagen().length);
    }

}
