package com.startlinesoft.icore.solidario.android.ais.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.startlinesoft.icore.solidario.ApiClient;
import com.startlinesoft.icore.solidario.ApiException;
import com.startlinesoft.icore.solidario.api.SocioApi;
import com.startlinesoft.icore.solidario.api.models.Socio;

public class SocioViewModel extends ViewModel {

    private MutableLiveData<Socio> socio;
    private ApiClient cliente;
    private SocioApi socioApi;

    public SocioViewModel(ApiClient cliente) {
        this.socio = new MutableLiveData<>();
        this.cliente = cliente;
        this.socioApi = new SocioApi(cliente);
    }

    private void recuperarSocioServicio(){
        new Thread(() -> {
            try {
                this.socio.postValue(socioApi.socio());
            } catch (ApiException ignored) {}
        }).start();
    }

    public LiveData<Socio> getSocio() {
        this.recuperarSocioServicio();
        return this.socio;
    }
}
