package com.startlinesoft.icore.solidario.android.ais.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.startlinesoft.icore.solidario.android.ais.R;

public class AhorroViewHolder extends RecyclerView.ViewHolder {

    private CardView cv;
    private TextView tvNombre;
    private TextView tvTasa;
    private TextView tvSaldo;

    public AhorroViewHolder(@NonNull View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cvAhorro);
        tvNombre = (TextView) itemView.findViewById(R.id.tvNombre);
        tvTasa = (TextView) itemView.findViewById(R.id.tvTasa);
        tvSaldo = (TextView) itemView.findViewById(R.id.tvSaldo);
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
}
