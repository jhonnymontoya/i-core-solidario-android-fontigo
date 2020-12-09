package com.startlinesoft.icore.solidario.android.ais.adapters.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.startlinesoft.icore.solidario.android.ais.R;
import com.startlinesoft.icore.solidario.android.ais.adapters.viewHolders.CreditoViewHolder;
import com.startlinesoft.icore.solidario.android.ais.listeners.ICoreRecyclerViewItemListener;
import com.startlinesoft.icore.solidario.api.models.Credito;

import java.util.List;

public class CreditoAdapter extends RecyclerView.Adapter<CreditoViewHolder> {

    private ICoreRecyclerViewItemListener listener;
    private List<Credito> creditos;

    public CreditoAdapter(List<Credito> creditos) {
        this.creditos = creditos;
    }

    @NonNull
    @Override
    public CreditoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.credito_item, parent, false);
        return new CreditoViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CreditoViewHolder holder, int position) {
        Credito credito = creditos.get(position);
        holder.getTvModalidad().setText(credito.getModalidad());
        holder.getTvNumeroObligacion().setText(credito.getNumeroObligacion());
        holder.getTvSaldo().setText(String.format("$%s", credito.getSaldoCapital()));

        holder.setPosicion(position);
        holder.setId(credito.getId());
    }

    @Override
    public int getItemCount() {
        return creditos.size();
    }

    public void setOnItemClickListener(ICoreRecyclerViewItemListener listener) {
        this.listener = listener;
    }
}
