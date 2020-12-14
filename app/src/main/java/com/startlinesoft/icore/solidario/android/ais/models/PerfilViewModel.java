package com.startlinesoft.icore.solidario.android.ais.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.startlinesoft.icore.solidario.ApiClient;
import com.startlinesoft.icore.solidario.ApiException;
import com.startlinesoft.icore.solidario.api.SocioApi;
import com.startlinesoft.icore.solidario.api.models.Perfil;
import com.startlinesoft.icore.solidario.api.models.Socio;

public class PerfilViewModel extends ViewModel {

    private MutableLiveData<Perfil> perfil;

    public PerfilViewModel(ApiClient cliente) {
        this.perfil = new MutableLiveData<>();
        SocioApi socioApi = new SocioApi(cliente);
        new Thread(() -> {
            try {
                this.perfil.postValue(socioApi.perfil());
            } catch (ApiException ignored) {
            }
        }).start();
    }

    public LiveData<Perfil> getSocio() {
        return this.perfil;
    }
}
