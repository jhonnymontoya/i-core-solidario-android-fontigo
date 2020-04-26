package com.startlinesoft.icore.solidario.android.ais.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.startlinesoft.icore.solidario.android.ais.R;

public class CreditoViewHolder extends RecyclerView.ViewHolder {

    private CardView cv;
    private TextView tvModalidad;
    private TextView tvNumeroObligacion;
    private TextView tvSaldo;

    public CreditoViewHolder(@NonNull View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cvCredito);
        tvModalidad = (TextView) itemView.findViewById(R.id.tvModalidad);
        tvNumeroObligacion = (TextView) itemView.findViewById(R.id.tvNumeroObligacion);
        tvSaldo = (TextView) itemView.findViewById(R.id.tvSaldo);
    }

    public CardView getCv() {
        return cv;
    }

    public TextView getTvModalidad() {
        return tvModalidad;
    }

    public TextView getTvNumeroObligacion() {
        return tvNumeroObligacion;
    }

    public TextView getTvSaldo() {
        return tvSaldo;
    }
}
