package com.startlinesoft.icore.solidario.android.ais.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.startlinesoft.icore.solidario.android.ais.R;
import com.startlinesoft.icore.solidario.android.ais.listeners.ICoreRecyclerViewItemListener;
import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreGeneral;
import com.startlinesoft.icore.solidario.api.models.Recaudo;

import java.util.ArrayList;
import java.util.List;

public class RecaudoAdapter extends RecyclerView.Adapter<RecaudoViewHolder> {

    private ICoreRecyclerViewItemListener listener;
    private List<Recaudo> recaudos = new ArrayList<Recaudo>();

    public RecaudoAdapter(List<Recaudo> recaudos) {
        this.recaudos = recaudos;
    }

    @NonNull
    @Override
    public RecaudoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recaudo_item, parent, false);
        RecaudoViewHolder rvh = new RecaudoViewHolder(v, listener);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecaudoViewHolder holder, int position) {
        Recaudo recaudo = recaudos.get(position);
        String fecha = ICoreGeneral.reverseFecha(recaudo.getFechaRecaudo());
        holder.getTvFechaAplicacion().setText(fecha);
        holder.getTvTotalAplicado().setText(String.format("$%s", recaudo.getTotalAplicado()));

        holder.setPosicion(position);
        holder.setId(null);
    }

    @Override
    public int getItemCount() {
        return recaudos.size();
    }

    public void setOnItemClickListener(ICoreRecyclerViewItemListener listener) {
        this.listener = listener;
    }
}
