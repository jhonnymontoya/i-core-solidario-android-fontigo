package com.startlinesoft.icore.solidario.android.ais;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.startlinesoft.icore.solidario.android.ais.enums.FuenteImagen;

public class CambiarImagenFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    public static final String TAG = "OpcionesCambiarImagen";

    private ItemClickListener mListener;
    private TextView tvCamara;
    private TextView tvGaleria;

    public static CambiarImagenFragment newInstance() {
        return new CambiarImagenFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cambiar_imagen_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.tvCamara = (TextView) view.findViewById(R.id.tvCamara);
        this.tvGaleria = (TextView) view.findViewById(R.id.tvGaleria);

        this.tvCamara.setOnClickListener(this);
        this.tvGaleria.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ItemClickListener) {
            mListener = (ItemClickListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        if(view.equals(this.tvCamara)){
            mListener.onItemClick(FuenteImagen.CAMARA);
        }
        if(view.equals(this.tvGaleria)){
            mListener.onItemClick(FuenteImagen.GALERIA);
        }
        dismiss();
    }

    public interface ItemClickListener {
        void onItemClick(FuenteImagen fuenteImagen);
    }
}
