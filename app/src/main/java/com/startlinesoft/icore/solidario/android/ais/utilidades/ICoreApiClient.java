package com.startlinesoft.icore.solidario.android.ais.utilidades;

import android.os.Build;

import com.startlinesoft.icore.solidario.ApiClient;
import com.startlinesoft.icore.solidario.Configuration;

public class ICoreApiClient {

    private static String token = "";

    public static ApiClient getApiClient() {
        ApiClient cliente = Configuration.getDefaultApiClient();
        //cliente.setBasePath("https://test.i-core.co/api");
        cliente.setUserAgent(ICoreApiClient.getFirmaDevice());
        return cliente;
    }

    public static void setToken(String token){
        ICoreApiClient.token = token;
    }

    private static String getFirmaDevice() {
        return new StringBuilder(Build.DEVICE)
                .append(";")
                .append(Build.MODEL)
                .append(";")
                .append(Build.PRODUCT)
                .append(";")
                .append(Build.VERSION.RELEASE)
                .toString();
    }
}
