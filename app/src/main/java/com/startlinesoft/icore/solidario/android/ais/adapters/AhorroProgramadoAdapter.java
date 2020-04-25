package com.startlinesoft.icore.solidario.android.ais.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.startlinesoft.icore.solidario.android.ais.R;
import com.startlinesoft.icore.solidario.api.models.AhorroProgramado;

import java.util.ArrayList;
import java.util.List;

public class AhorroProgramadoAdapter extends RecyclerView.Adapter<AhorroViewHolder> {

    List<AhorroProgramado> ahorros = new ArrayList<AhorroProgramado>();

    public AhorroProgramadoAdapter(List<AhorroProgramado> ahorros) {
        this.ahorros = ahorros;
    }

    @NonNull
    @Override
    public AhorroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ahorro_item, parent, false);
        AhorroViewHolder avh = new AhorroViewHolder(v);
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull AhorroViewHolder holder, int position) {
        AhorroProgramado ahorro = ahorros.get(position);
        holder.getTvNombre().setText(ahorro.getModalidad());
        holder.getTvTasa().setText(String.format("%s%%", ahorro.getTasaEA()));
        holder.getTvSaldo().setText(String.format("$%s", ahorro.getSaldo()));
    }

    @Override
    public int getItemCount() {
        return ahorros.size();
    }
}
