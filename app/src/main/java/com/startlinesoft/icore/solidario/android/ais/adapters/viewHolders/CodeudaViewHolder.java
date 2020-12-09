package com.startlinesoft.icore.solidario.android.ais.adapters.viewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.startlinesoft.icore.solidario.android.ais.R;
import com.startlinesoft.icore.solidario.android.ais.enums.TipoRecyclerViewItem;
import com.startlinesoft.icore.solidario.android.ais.listeners.ICoreRecyclerViewItemListener;

public class CodeudaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ICoreRecyclerViewItemListener listener;

    private int posicion;
    private Integer id;
    private CardView cv;
    private TextView tvDeudor;
    private TextView tvNumeroObligacion;
    private TextView tvSaldo;

    public CodeudaViewHolder(@NonNull View itemView, ICoreRecyclerViewItemListener listener) {
        super(itemView);
        this.listener = listener;
        cv = (CardView) itemView.findViewById(R.id.cvCodeuda);
        tvDeudor = (TextView) itemView.findViewById(R.id.tvDeudor);
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

    public TextView getTvDeudor() {
        return tvDeudor;
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
            listener.onRecyclerViewItemClick(v, posicion, id, TipoRecyclerViewItem.CODEUDA);
        }
    }
}
