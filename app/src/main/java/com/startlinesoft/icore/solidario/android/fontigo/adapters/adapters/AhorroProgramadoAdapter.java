package com.startlinesoft.icore.solidario.android.fontigo.adapters.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.startlinesoft.icore.solidario.android.fontigo.R;
import com.startlinesoft.icore.solidario.android.fontigo.adapters.viewHolders.AhorroViewHolder;
import com.startlinesoft.icore.solidario.android.fontigo.enums.TipoRecyclerViewItem;
import com.startlinesoft.icore.solidario.android.fontigo.listeners.ICoreRecyclerViewItemListener;
import com.startlinesoft.icore.solidario.api.models.AhorroProgramado;

import java.util.List;

public class AhorroProgramadoAdapter extends RecyclerView.Adapter<AhorroViewHolder> {

    private ICoreRecyclerViewItemListener listener;
    List<AhorroProgramado> ahorros;

    public AhorroProgramadoAdapter(List<AhorroProgramado> ahorros) {
        this.ahorros = ahorros;
    }

    @NonNull
    @Override
    public AhorroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ahorro_item, parent, false);
        return new AhorroViewHolder(v, listener, TipoRecyclerViewItem.AHORRO_PROGRAMADO);
    }

    @Override
    public void onBindViewHolder(@NonNull AhorroViewHolder holder, int position) {
        AhorroProgramado ahorro = this.ahorros.get(position);
        holder.getTvNombre().setText(ahorro.getModalidad());
        holder.getTvTasa().setText(String.format("%s%%", ahorro.getTasaEA()));
        holder.getTvSaldo().setText(String.format("$%s", ahorro.getSaldo()));

        holder.setPosicion(position);
        holder.setId(ahorro.getId());
    }

    @Override
    public int getItemCount() {
        return this.ahorros.size();
    }

    public void setOnItemClickListener(ICoreRecyclerViewItemListener listener) {
        this.listener = listener;
    }

}
