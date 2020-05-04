package com.startlinesoft.icore.solidario.android.ais.adapters.viewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.startlinesoft.icore.solidario.android.ais.R;
import com.startlinesoft.icore.solidario.android.ais.enums.TipoRecyclerViewItem;
import com.startlinesoft.icore.solidario.android.ais.listeners.ICoreRecyclerViewItemListener;

public class RecaudoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ICoreRecyclerViewItemListener listener;

    private int posicion;
    private Integer id;
    private CardView cv;
    private TextView tvFechaAplicacion;
    private TextView tvTotalAplicado;

    public RecaudoViewHolder(@NonNull View itemView, ICoreRecyclerViewItemListener listener) {
        super(itemView);
        this.listener = listener;
        cv = (CardView) itemView.findViewById(R.id.cvRecaudo);
        tvFechaAplicacion = (TextView) itemView.findViewById(R.id.tvFechaAplicacion);
        tvTotalAplicado = (TextView) itemView.findViewById(R.id.tvTotalAplicado);

        cv.setOnClickListener(this);
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onRecyclerViewItemClick(v, posicion, id, TipoRecyclerViewItem.RECAUDO);
        }
    }
}
