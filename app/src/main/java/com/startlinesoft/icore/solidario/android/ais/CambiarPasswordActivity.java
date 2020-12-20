package com.startlinesoft.icore.solidario.android.ais;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.startlinesoft.icore.solidario.android.ais.databinding.ActivityCambiarpasswordBinding;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreAppCompatActivity;

public class CambiarPasswordActivity extends ICoreAppCompatActivity implements View.OnClickListener {

    private ActivityCambiarpasswordBinding bnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bnd = ActivityCambiarpasswordBinding.inflate(this.getLayoutInflater());
        this.setContentView(this.bnd.getRoot());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}