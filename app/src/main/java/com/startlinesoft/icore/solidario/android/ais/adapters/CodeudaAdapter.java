package com.startlinesoft.icore.solidario.android.ais.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.startlinesoft.icore.solidario.android.ais.R;
import com.startlinesoft.icore.solidario.api.models.Codeuda;

import java.util.ArrayList;
import java.util.List;

public class CodeudaAdapter extends RecyclerView.Adapter<CodeudaViewHolder> {

    private List<Codeuda> codeudas = new ArrayList<Codeuda>();

    public CodeudaAdapter(List<Codeuda> codeudas) {
        this.codeudas = codeudas;
    }

    @NonNull
    @Override
    public CodeudaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.codeuda_item, parent, false);
        CodeudaViewHolder cvh = new CodeudaViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CodeudaViewHolder holder, int position) {
        Codeuda codeuda = codeudas.get(position);
        holder.getTvDeudor().setText(codeuda.getDeudor());
        holder.getTvNumeroObligacion().setText(codeuda.getNumeroObligacion());
        holder.getTvSaldo().setText(String.format("$%s", codeuda.getSaldoCapital()));
    }

    @Override
    public int getItemCount() {
        return codeudas.size();
    }
}
