package com.startlinesoft.icore.solidario.android.ais.adapters.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.startlinesoft.icore.solidario.android.ais.R;
import com.startlinesoft.icore.solidario.android.ais.adapters.viewHolders.CodeudaViewHolder;
import com.startlinesoft.icore.solidario.android.ais.listeners.ICoreRecyclerViewItemListener;
import com.startlinesoft.icore.solidario.api.models.Codeuda;

import java.util.List;

public class CodeudaAdapter extends RecyclerView.Adapter<CodeudaViewHolder> {

    private ICoreRecyclerViewItemListener listener;
    private List<Codeuda> codeudas;

    public CodeudaAdapter(List<Codeuda> codeudas) {
        this.codeudas = codeudas;
    }

    @NonNull
    @Override
    public CodeudaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.codeuda_item, parent, false);
        return new CodeudaViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CodeudaViewHolder holder, int position) {
        Codeuda codeuda = codeudas.get(position);
        holder.getTvDeudor().setText(codeuda.getDeudor());
        holder.getTvNumeroObligacion().setText(codeuda.getNumeroObligacion());
        holder.getTvSaldo().setText(String.format("$%s", codeuda.getSaldoCapital()));

        holder.setPosicion(position);
        holder.setId(null);
    }

    @Override
    public int getItemCount() {
        return codeudas.size();
    }

    public void setOnItemClickListener(ICoreRecyclerViewItemListener listener) {
        this.listener = listener;
    }

}
