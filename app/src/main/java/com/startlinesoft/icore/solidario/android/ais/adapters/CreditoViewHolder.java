package com.startlinesoft.icore.solidario.android.ais.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.startlinesoft.icore.solidario.android.ais.R;
import com.startlinesoft.icore.solidario.android.ais.enums.TipoRecyclerViewItem;
import com.startlinesoft.icore.solidario.android.ais.listeners.ICoreRecyclerViewItemListener;

public class CreditoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ICoreRecyclerViewItemListener listener;

    private int posicion;
    private Integer id;
    private CardView cv;
    private TextView tvModalidad;
    private TextView tvNumeroObligacion;
    private TextView tvSaldo;

    public CreditoViewHolder(@NonNull View itemView, ICoreRecyclerViewItemListener listener) {
        super(itemView);
        this.listener = listener;
        cv = (CardView) itemView.findViewById(R.id.cvCredito);
        tvModalidad = (TextView) itemView.findViewById(R.id.tvModalidad);
        tvNumeroObligacion = (TextView) itemView.findViewById(R.id.tvNumeroObligacion);
        tvSaldo = (TextView) itemView.findViewById(R.id.tvSaldo);

        cv.setOnClickListener(this);
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onRecyclerViewItemClick(v, posicion, id, TipoRecyclerViewItem.CREDITO);
        }
    }
}
