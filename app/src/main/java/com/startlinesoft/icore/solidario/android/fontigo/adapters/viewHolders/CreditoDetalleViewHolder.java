package com.startlinesoft.icore.solidario.android.fontigo.adapters.viewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.startlinesoft.icore.solidario.android.fontigo.R;

public class CreditoDetalleViewHolder extends RecyclerView.ViewHolder {

    private TextView tvConcepto;
    private TextView tvFecha;
    private TextView tvValorPositivo;
    private TextView tvValorNegativo;

    public CreditoDetalleViewHolder(@NonNull View itemView) {
        super(itemView);
        tvConcepto = (TextView) itemView.findViewById(R.id.tvConcepto);
        tvFecha = (TextView) itemView.findViewById(R.id.tvFecha);
        tvValorPositivo = (TextView) itemView.findViewById(R.id.tvValorPositivo);
        tvValorNegativo = (TextView) itemView.findViewById(R.id.tvValorNegativo);
    }

    public TextView getTvConcepto() {
        return tvConcepto;
    }

    public TextView getTvFecha() {
        return tvFecha;
    }

    public TextView getTvValorPositivo() {
        return tvValorPositivo;
    }

    public TextView getTvValorNegativo() {
        return tvValorNegativo;
    }

}
