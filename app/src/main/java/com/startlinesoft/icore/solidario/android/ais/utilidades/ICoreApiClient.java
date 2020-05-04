package com.startlinesoft.icore.solidario.android.ais.utilidades;

import com.startlinesoft.icore.solidario.ApiClient;
import com.startlinesoft.icore.solidario.ApiException;
import com.startlinesoft.icore.solidario.Configuration;
import com.startlinesoft.icore.solidario.api.LoginApi;
import com.startlinesoft.icore.solidario.auth.HttpBearerAuth;

import java.util.Locale;

public class ICoreApiClient {

    private static String token = "";

    public static ApiClient getApiClient() {
        ApiClient cliente = Configuration.getDefaultApiClient();
        //cliente.setBasePath("https://test.i-core.co/api");
        cliente.setUserAgent(ICoreApiClient.getFirmaDevice());

        //Adicion de versión de api al cliente
        cliente.addDefaultHeader("apiVersion", "1.0.0");

        //Adicion de idioma del dispositivo
        cliente.addDefaultHeader("codigoIdioma", Locale.getDefault().getISO3Language());

        if(ICoreApiClient.token.length() > 0) {
            HttpBearerAuth auth = (HttpBearerAuth) cliente.getAuthentication("icore_auth");
            auth.setBearerToken(ICoreApiClient.token);
        }
        return cliente;
    }

    /**
     * Establece el token de autenticación
     * @param token
     */
    public static void setToken(String token){
        ICoreApiClient.token = token;
    }

    /**
     * Verifica si el token de autenticación es o no válido y vigente
     * @return
     */
    public static boolean esTokenValido() {
        LoginApi loginApi = new LoginApi(ICoreApiClient.getApiClient());
        ICoreApiClient.token = "";
        try {
            loginApi.ping();
            return true;
        } catch (ApiException e) {
            if(e.getCode() == 401) {
                return false;
            }
            else {
                return false;
            }
        }
    }

    private static String getFirmaDevice() {
        return System.getProperty("http.agent");
    }
}
