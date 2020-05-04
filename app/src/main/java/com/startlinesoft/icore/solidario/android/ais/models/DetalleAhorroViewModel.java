package com.startlinesoft.icore.solidario.android.ais.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.startlinesoft.icore.solidario.ApiClient;
import com.startlinesoft.icore.solidario.ApiException;
import com.startlinesoft.icore.solidario.api.AhorrosApi;
import com.startlinesoft.icore.solidario.api.models.DetalleAhorro;

public class DetalleAhorroViewModel extends ViewModel {

    private MutableLiveData<DetalleAhorro> detalleAhorro;

    public DetalleAhorroViewModel(ApiClient cliente, Integer ahorroId){
        detalleAhorro = new MutableLiveData<DetalleAhorro>();
        AhorrosApi ahorrosApi = new AhorrosApi(cliente);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    detalleAhorro.postValue(ahorrosApi.obtenerAhorro(ahorroId));
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public LiveData<DetalleAhorro> getDetalleAhorro() {
        return detalleAhorro;
    }
}
