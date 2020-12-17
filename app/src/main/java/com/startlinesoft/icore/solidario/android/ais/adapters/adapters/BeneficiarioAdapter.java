package com.startlinesoft.icore.solidario.android.ais.adapters.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.startlinesoft.icore.solidario.android.ais.R;
import com.startlinesoft.icore.solidario.android.ais.adapters.viewHolders.BeneficiarioItemHolder;
import com.startlinesoft.icore.solidario.api.models.Beneficiario;

import java.util.List;

public class BeneficiarioAdapter extends RecyclerView.Adapter<BeneficiarioItemHolder> {

    private List<Beneficiario> beneficiarios;

    public BeneficiarioAdapter(List<Beneficiario> beneficiarios) {
        this.beneficiarios = beneficiarios;
    }

    @NonNull
    @Override
    public BeneficiarioItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.beneficiario_item, parent, false);
        return new BeneficiarioItemHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BeneficiarioItemHolder holder, int position) {
        Beneficiario beneficiario = this.beneficiarios.get(position);
        /*
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
         */
    }

    @Override
    public int getItemCount() {
        return this.beneficiarios.size();
    }
}
