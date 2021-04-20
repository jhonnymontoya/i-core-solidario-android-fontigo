package com.startlinesoft.icore.solidario.android.fontigo.utilidades;

import com.startlinesoft.icore.solidario.ApiCallback;
import com.startlinesoft.icore.solidario.ApiClient;
import com.startlinesoft.icore.solidario.ApiException;
import com.startlinesoft.icore.solidario.Configuration;
import com.startlinesoft.icore.solidario.api.LoginApi;
import com.startlinesoft.icore.solidario.auth.HttpBearerAuth;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ICoreApiClient {

    private static String token = "";
    private static final String BASEPATH = "https://%s.i-core.co/api";
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
        String basePath = String.format(ICoreApiClient.BASEPATH, ICoreConstantes.PROYECTO);
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
        final boolean[] res = {false};
        Thread t = new Thread(() ->{
            try {
                loginApi.ping();
                res[0] = true;
            } catch (ApiException e) {
                res[0] = false;
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException ignored) {}
        return res[0];
    }

}
