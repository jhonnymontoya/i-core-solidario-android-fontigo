package com.startlinesoft.icore.solidario.android.ais.utilidades;

public class ICoreGeneral {

    /**
     * Retorna una fecha de formato yyyy-mm-dd a dd-mm-yyyy
     * @param fecha
     * @return fecha
     */
    public static String reverseFecha(String fecha) {
        String[] tmp = fecha.split("-");
        if(tmp.length != 3) {
            return fecha;
        }
        String fechaFormateada = "%s-%s-%s";
        fechaFormateada = String.format(fechaFormateada, tmp[2], tmp[1], tmp[0]);
        return fechaFormateada;
    }
}
