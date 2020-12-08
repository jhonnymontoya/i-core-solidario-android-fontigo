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

    public SocioViewModel(ApiClient cliente) {
        this.socio = new MutableLiveData<>();
        SocioApi socioApi = new SocioApi(cliente);
        new Thread(() -> {
            try {
                this.socio.postValue(socioApi.socio());
            } catch (ApiException ignored) {
            }
        }).start();
    }

    public LiveData<Socio> getSocio() {
        return this.socio;
    }
}
