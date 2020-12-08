package com.startlinesoft.icore.solidario.android.ais.models;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreApiClient;

public class SocioViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SocioViewModel.class)) {
            return (T) new SocioViewModel(ICoreApiClient.getApiClient());
        } else {
            throw new IllegalArgumentException("Clase ViewModel desconocida");
        }
    }
}
