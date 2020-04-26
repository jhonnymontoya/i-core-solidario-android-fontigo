package com.startlinesoft.icore.solidario.android.ais.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.startlinesoft.icore.solidario.android.ais.R;
import com.startlinesoft.icore.solidario.android.ais.enums.TipoRecyclerViewItem;
import com.startlinesoft.icore.solidario.android.ais.listeners.ICoreRecyclerViewItemListener;
import com.startlinesoft.icore.solidario.api.models.AhorroGeneral;

import java.util.ArrayList;
import java.util.List;

public class AhorroGeneralAdapter extends RecyclerView.Adapter<AhorroViewHolder> {

    private ICoreRecyclerViewItemListener listener;
    private List<AhorroGeneral> ahorros = new ArrayList<AhorroGeneral>();

    public AhorroGeneralAdapter(List<AhorroGeneral> ahorros) {
        this.ahorros = ahorros;
    }

    @NonNull
    @Override
    public AhorroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ahorro_item, parent, false);
        AhorroViewHolder avh = new AhorroViewHolder(v, listener, TipoRecyclerViewItem.AHORRO_GENERAL);
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull AhorroViewHolder holder, int position) {
        AhorroGeneral ahorro = ahorros.get(position);
        holder.getTvNombre().setText(ahorro.getModalidad());
        holder.getTvTasa().setText(String.format("%s%%", ahorro.getTasaEA()));
        holder.getTvSaldo().setText(String.format("$%s", ahorro.getSaldo()));

        holder.setPosicion(position);
        holder.setId(ahorro.getId());
    }

    @Override
    public int getItemCount() {
        return ahorros.size();
    }

    public void setOnItemClickListener(ICoreRecyclerViewItemListener listener) {
        this.listener = listener;
    }
}
