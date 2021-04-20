package com.startlinesoft.icore.solidario.android.fontigo.adapters.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.startlinesoft.icore.solidario.android.fontigo.R;
import com.startlinesoft.icore.solidario.android.fontigo.adapters.viewHolders.CreditoDetalleViewHolder;
import com.startlinesoft.icore.solidario.android.fontigo.utilidades.ICoreGeneral;
import com.startlinesoft.icore.solidario.api.models.MovimientoCredito;
import com.startlinesoft.icore.solidario.api.models.Signo;

import java.util.List;

public class CreditoDetalleAdapter extends RecyclerView.Adapter<CreditoDetalleViewHolder> {

    List<MovimientoCredito> movimientosCredito;

    public CreditoDetalleAdapter(List<MovimientoCredito> movimientosCredito) {
        this.movimientosCredito = movimientosCredito;
    }

    @NonNull
    @Override
    public CreditoDetalleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.credito_detalle_item, parent, false);
        return new CreditoDetalleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CreditoDetalleViewHolder holder, int position) {
        MovimientoCredito movimientoCredito = movimientosCredito.get(position);
        holder.getTvConcepto().setText(movimientoCredito.getConcepto());
        holder.getTvFecha().setText(ICoreGeneral.reverseFecha(movimientoCredito.getFecha().toString()));
        if (movimientoCredito.getSignoCapital() == Signo.POSITIVO) {
            holder.getTvValorPositivo().setText(String.format("$%s", movimientoCredito.getCapital()));
            holder.getTvValorPositivo().setVisibility(View.VISIBLE);
            holder.getTvValorNegativo().setVisibility(View.GONE);
        } else {
            holder.getTvValorNegativo().setText(String.format("$%s", movimientoCredito.getCapital()));
            holder.getTvValorPositivo().setVisibility(View.GONE);
            holder.getTvValorNegativo().setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return movimientosCredito.size();
    }

}
