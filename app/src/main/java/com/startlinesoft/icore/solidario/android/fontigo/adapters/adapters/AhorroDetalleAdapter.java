package com.startlinesoft.icore.solidario.android.fontigo.adapters.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.startlinesoft.icore.solidario.android.fontigo.R;
import com.startlinesoft.icore.solidario.android.fontigo.adapters.viewHolders.AhorroDetalleViewHolder;
import com.startlinesoft.icore.solidario.android.fontigo.utilidades.ICoreGeneral;
import com.startlinesoft.icore.solidario.api.models.MovimientoAhorro;
import com.startlinesoft.icore.solidario.api.models.Signo;

import java.util.List;

public class AhorroDetalleAdapter extends RecyclerView.Adapter<AhorroDetalleViewHolder> {

    List<MovimientoAhorro> movimientosAhorros;

    public AhorroDetalleAdapter(List<MovimientoAhorro> movimientosAhorros) {
        this.movimientosAhorros = movimientosAhorros;
    }

    @NonNull
    @Override
    public AhorroDetalleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ahorro_detalle_item, parent, false);
        return new AhorroDetalleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AhorroDetalleViewHolder holder, int position) {
        MovimientoAhorro movimientoAhorro = movimientosAhorros.get(position);
        holder.getTvConcepto().setText(movimientoAhorro.getConcepto());
        holder.getTvFecha().setText(ICoreGeneral.reverseFecha(movimientoAhorro.getFecha().toString()));
        if (movimientoAhorro.getSignoValor() == Signo.POSITIVO) {
            holder.getTvValorPositivo().setText(String.format("$%s", movimientoAhorro.getValor()));
            holder.getTvValorPositivo().setVisibility(View.VISIBLE);
            holder.getTvValorNegativo().setVisibility(View.GONE);
        } else {
            holder.getTvValorNegativo().setText(String.format("$%s", movimientoAhorro.getValor()));
            holder.getTvValorPositivo().setVisibility(View.GONE);
            holder.getTvValorNegativo().setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return movimientosAhorros.size();
    }

}
