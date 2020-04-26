package com.startlinesoft.icore.solidario.android.ais.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.startlinesoft.icore.solidario.android.ais.R;

public class RecaudoViewHolder extends RecyclerView.ViewHolder {

    private CardView cv;
    private TextView tvFechaAplicacion;
    private TextView tvTotalAplicado;

    public RecaudoViewHolder(@NonNull View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cvRecaudo);
        tvFechaAplicacion = (TextView) itemView.findViewById(R.id.tvFechaAplicacion);
        tvTotalAplicado = (TextView) itemView.findViewById(R.id.tvTotalAplicado);
    }

    public CardView getCv() {
        return cv;
    }

    public TextView getTvFechaAplicacion() {
        return tvFechaAplicacion;
    }

    public TextView getTvTotalAplicado() {
        return tvTotalAplicado;
    }
}
