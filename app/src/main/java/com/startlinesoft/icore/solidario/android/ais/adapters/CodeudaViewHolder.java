package com.startlinesoft.icore.solidario.android.ais.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.startlinesoft.icore.solidario.android.ais.R;

public class CodeudaViewHolder extends RecyclerView.ViewHolder {

    private CardView cv;
    private TextView tvDeudor;
    private TextView tvNumeroObligacion;
    private TextView tvSaldo;

    public CodeudaViewHolder(@NonNull View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cvCodeuda);
        tvDeudor = (TextView) itemView.findViewById(R.id.tvDeudor);
        tvNumeroObligacion = (TextView) itemView.findViewById(R.id.tvNumeroObligacion);
        tvSaldo = (TextView) itemView.findViewById(R.id.tvSaldo);
    }

    public CardView getCv() {
        return cv;
    }

    public TextView getTvDeudor() {
        return tvDeudor;
    }

    public TextView getTvNumeroObligacion() {
        return tvNumeroObligacion;
    }

    public TextView getTvSaldo() {
        return tvSaldo;
    }
}
