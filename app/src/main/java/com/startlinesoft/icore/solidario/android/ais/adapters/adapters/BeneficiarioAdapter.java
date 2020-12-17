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

        String porcentaje = "%s%%";
        porcentaje = String.format(
                porcentaje,
                beneficiario.getPorcentajeBeneficio()
        );
        holder.getTvPorcentaje().setText(porcentaje);

        String identificacion = "%s %s";
        identificacion = String.format(
                identificacion,
                beneficiario.getCodigoTipoIdentificacion(),
                beneficiario.getIdentificacion()
        );
        holder.getTvIdentificacion().setText(identificacion);

        holder.getTvNombre().setText(beneficiario.getNombreCompleto());

        holder.getTvParentesco().setText(beneficiario.getParentesco());
    }

    @Override
    public int getItemCount() {
        return this.beneficiarios.size();
    }
}
