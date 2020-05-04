package com.startlinesoft.icore.solidario.android.ais.models;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreApiClient;

public class DetalleAhorroViewModelFactory implements ViewModelProvider.Factory {

    private Integer ahorroId;

    public DetalleAhorroViewModelFactory(Integer ahorroId) {
        this.ahorroId = ahorroId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DetalleAhorroViewModel.class)) {
            return (T) new DetalleAhorroViewModel(ICoreApiClient.getApiClient(), ahorroId);
        }
        else {
            throw  new IllegalArgumentException("Clase ViewModel desconocida");
        }
    }
}
