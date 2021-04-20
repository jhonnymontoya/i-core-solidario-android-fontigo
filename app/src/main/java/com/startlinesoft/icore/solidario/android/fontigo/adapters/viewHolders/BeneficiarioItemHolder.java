package com.startlinesoft.icore.solidario.android.fontigo.adapters.viewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.startlinesoft.icore.solidario.android.fontigo.R;

public class BeneficiarioItemHolder extends RecyclerView.ViewHolder {

    private TextView tvPorcentaje;
    private TextView tvIdentificacion;
    private TextView tvNombre;
    private TextView tvParentesco;

    public BeneficiarioItemHolder(@NonNull View itemView) {
        super(itemView);
        this.tvPorcentaje = (TextView) itemView.findViewById(R.id.tvPorcentaje);
        this.tvIdentificacion = (TextView) itemView.findViewById(R.id.tvIdentificacion);
        this.tvNombre = (TextView) itemView.findViewById(R.id.tvNombre);
        this.tvParentesco = (TextView) itemView.findViewById(R.id.tvParentesco);
    }

    public TextView getTvPorcentaje() {
        return this.tvPorcentaje;
    }

    public TextView getTvIdentificacion() {
        return this.tvIdentificacion;
    }

    public TextView getTvNombre() {
        return this.tvNombre;
    }

    public TextView getTvParentesco() {
        return this.tvParentesco;
    }
}
