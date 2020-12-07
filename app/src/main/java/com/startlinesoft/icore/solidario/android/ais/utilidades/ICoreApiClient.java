package com.startlinesoft.icore.solidario.android.ais.utilidades;

import com.startlinesoft.icore.solidario.ApiClient;
import com.startlinesoft.icore.solidario.ApiException;
import com.startlinesoft.icore.solidario.Configuration;
import com.startlinesoft.icore.solidario.api.LoginApi;
import com.startlinesoft.icore.solidario.auth.HttpBearerAuth;

import java.util.Locale;

public class ICoreApiClient {

    private static String token = "";
    private static final String BASEPATH = "https://%s.i-core.co/api";
    private static final String PROYECTO = "test";
    private static final String APIVERSION = "1.0.0";

    public static ApiClient getApiClient() {
        ApiClient cliente = Configuration.getDefaultApiClient();
        cliente.setBasePath(ICoreApiClient.getBasePath());
        cliente.setUserAgent(ICoreApiClient.getFirmaDevice());

        //Adicion de versión de api al cliente
        cliente.addDefaultHeader("apiVersion", ICoreApiClient.APIVERSION);

        //Adicion de idioma del dispositivo
        cliente.addDefaultHeader("codigoIdioma", Locale.getDefault().getISO3Language());

        if (ICoreApiClient.token.isEmpty() == false) {
            HttpBearerAuth auth = (HttpBearerAuth) cliente.getAuthentication("icore_auth");
            auth.setBearerToken(ICoreApiClient.token);
        }

        return cliente;
    }

    private static String getBasePath() {
        String basePath = String.format(ICoreApiClient.BASEPATH, ICoreApiClient.PROYECTO);
        return basePath;
    }

    public static void setToken(String token) {
        ICoreApiClient.token = token;
    }

    private static String getFirmaDevice() {
        return System.getProperty("http.agent");
    }

    public static boolean esTokenValido() {
        LoginApi loginApi = new LoginApi(ICoreApiClient.getApiClient());
        try {
            loginApi.ping();
            return true;
        } catch (ApiException e) {
            if (e.getCode() == 401) {
                return false;
            } else {
                return false;
            }
        }
    }

}
