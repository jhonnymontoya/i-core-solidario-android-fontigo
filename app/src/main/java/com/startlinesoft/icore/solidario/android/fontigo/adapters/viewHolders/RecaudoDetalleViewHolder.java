package com.startlinesoft.icore.solidario.android.fontigo.adapters.viewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.startlinesoft.icore.solidario.android.fontigo.R;

public class RecaudoDetalleViewHolder extends RecyclerView.ViewHolder {

    private TextView tvNombreConcepto;
    private TextView tvAplicado;

    public RecaudoDetalleViewHolder(@NonNull View itemView) {
        super(itemView);
        tvNombreConcepto = (TextView) itemView.findViewById(R.id.tvNombreConcepto);
        tvAplicado = (TextView) itemView.findViewById(R.id.tvAplicado);
    }

    public TextView getTvNombreConcepto() {
        return tvNombreConcepto;
    }

    public TextView getTvAplicado() {
        return tvAplicado;
    }
}
