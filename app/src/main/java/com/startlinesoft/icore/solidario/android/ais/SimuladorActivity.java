package com.startlinesoft.icore.solidario.android.ais;

import android.os.Bundle;
import android.view.View;

import com.startlinesoft.icore.solidario.android.ais.databinding.ActivitySimuladorBinding;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;

public class SimuladorActivity extends ICoreAppCompatActivity implements View.OnClickListener {

    private ActivitySimuladorBinding bnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bnd = ActivitySimuladorBinding.inflate(this.getLayoutInflater());
        this.setContentView(this.bnd.getRoot());
    }
}