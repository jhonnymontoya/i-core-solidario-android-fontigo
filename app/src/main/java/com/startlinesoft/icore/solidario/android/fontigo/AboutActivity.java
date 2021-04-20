package com.startlinesoft.icore.solidario.android.fontigo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.startlinesoft.icore.solidario.android.fontigo.databinding.ActivityAboutBinding;
import com.startlinesoft.icore.solidario.android.fontigo.utilidades.ICoreAppCompatActivity;

public class AboutActivity extends ICoreAppCompatActivity implements View.OnClickListener {

    private ActivityAboutBinding bnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bnd =ActivityAboutBinding.inflate(this.getLayoutInflater());
        this.setContentView(this.bnd.getRoot());

        String version = "0.0.0";
        try {
            PackageInfo pInfo = this.getApplicationContext().getPackageManager().getPackageInfo(this.getApplicationContext().getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }

        String nombreVerion = "I-Core %s %s";
        nombreVerion = String.format(
                nombreVerion,
                this.getString(R.string.app_name),
                version
        );

        this.bnd.tvNombreVersion.setText(nombreVerion);

        this.bnd.tvStartLineSoft.setText(this.getText(R.string.powered_by));
        this.bnd.btnCancelar.setOnClickListener(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.removerTituloBarra();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if(v.equals(this.bnd.tvStartLineSoft)){
            String url = "https://startlinesoft.com";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
            return;
        }

        if(v.equals(this.bnd.btnCancelar)){
            this.finish();
            return;
        }
    }
}