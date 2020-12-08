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
        this.tipo = tipo;

        this.cv = (CardView) itemView.findViewById(R.id.cvAhorro);
        this.tvNombre = (TextView) itemView.findViewById(R.id.tvNombre);
        this.tvTasa = (TextView) itemView.findViewById(R.id.tvTasa);
        this.tvSaldo = (TextView) itemView.findViewById(R.id.tvSaldo);

        this.cv.setOnClickListener(this);
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CardView getCv() {
        return this.cv;
    }

    public TextView getTvNombre() {
        return this.tvNombre;
    }

    public TextView getTvTasa() {
        return this.tvTasa;
    }

    public TextView getTvSaldo() {
        return this.tvSaldo;
    }

    @Override
    public void onClick(View v) {
        if (this.listener != null) {
            this.listener.onRecyclerViewItemClick(v, this.posicion, this.id, this.tipo);
        }
    }
}
