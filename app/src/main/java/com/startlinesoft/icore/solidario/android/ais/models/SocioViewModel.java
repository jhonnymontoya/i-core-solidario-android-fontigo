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
        socio = new MutableLiveData<Socio>();
        SocioApi socioApi = new SocioApi(cliente);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socio.postValue(socioApi.socio());;
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public LiveData<Socio> getSocio() {
        return socio;
    }
}
