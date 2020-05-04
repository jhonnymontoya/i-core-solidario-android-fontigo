package com.startlinesoft.icore.solidario.android.ais.adapters.viewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.startlinesoft.icore.solidario.android.ais.R;
import com.startlinesoft.icore.solidario.android.ais.enums.TipoRecyclerViewItem;
import com.startlinesoft.icore.solidario.android.ais.listeners.ICoreRecyclerViewItemListener;

public class AhorroViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ICoreRecyclerViewItemListener listener;

    private int posicion;
    private Integer id;
    private CardView cv;
    private TextView tvNombre;
    private TextView tvTasa;
    private TextView tvSaldo;
    private TipoRecyclerViewItem tipo;

    public AhorroViewHolder(@NonNull View itemView, ICoreRecyclerViewItemListener listener, TipoRecyclerViewItem tipo) {
        super(itemView);
        this.listener = listener;
        cv = (CardView) itemView.findViewById(R.id.cvAhorro);
        tvNombre = (TextView) itemView.findViewById(R.id.tvNombre);
        tvTasa = (TextView) itemView.findViewById(R.id.tvTasa);
        tvSaldo = (TextView) itemView.findViewById(R.id.tvSaldo);

        this.tipo = tipo;
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

    public TextView getTvNombre() {
        return tvNombre;
    }

    public TextView getTvTasa() {
        return tvTasa;
    }

    public TextView getTvSaldo() {
        return tvSaldo;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onRecyclerViewItemClick(v, posicion, id, tipo);
        }
    }
}
