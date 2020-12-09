package com.startlinesoft.icore.solidario.android.ais.adapters.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.startlinesoft.icore.solidario.android.ais.R;
import com.startlinesoft.icore.solidario.android.ais.adapters.viewHolders.RecaudoDetalleViewHolder;
import com.startlinesoft.icore.solidario.api.models.ConceptoRecaudo;

import java.util.List;

public class RecaudoDetalleAdapter extends RecyclerView.Adapter<RecaudoDetalleViewHolder> {

    List<ConceptoRecaudo> conceptos;

    public RecaudoDetalleAdapter(List<ConceptoRecaudo> conceptos) {
        this.conceptos = conceptos;
    }

    @NonNull
    @Override
    public RecaudoDetalleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recaudo_detalle_item, parent, false);
        return new RecaudoDetalleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecaudoDetalleViewHolder holder, int position) {
        ConceptoRecaudo conceptoRecaudo = conceptos.get(position);
        holder.getTvNombreConcepto().setText(conceptoRecaudo.getNombre());
        holder.getTvAplicado().setText(String.format("$%s", conceptoRecaudo.getAplicado()));
    }

    @Override
    public int getItemCount() {
        return conceptos.size();
    }

}
