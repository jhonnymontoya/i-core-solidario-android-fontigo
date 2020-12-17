package com.startlinesoft.icore.solidario.android.ais.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.startlinesoft.icore.solidario.ApiClient;
import com.startlinesoft.icore.solidario.ApiException;
import com.startlinesoft.icore.solidario.api.SocioApi;
import com.startlinesoft.icore.solidario.api.models.Beneficiario;

import java.util.List;

public class BeneficiarioViewModel extends ViewModel {

    private MutableLiveData<List<Beneficiario>> beneficiarios;

    public BeneficiarioViewModel(ApiClient apiClient) {
        this.beneficiarios = new MutableLiveData<>();
        SocioApi socioApi = new SocioApi(apiClient);
        new Thread(() -> {
            try {
                this.beneficiarios.postValue(socioApi.beneficiarios());
            } catch (ApiException ignored) {
            }
        }).start();
    }

    public LiveData<List<Beneficiario>> getBeneficiarios() {
        return this.beneficiarios;
    }
}
