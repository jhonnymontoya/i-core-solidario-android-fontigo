package com.startlinesoft.icore.solidario.android.fontigo.listeners;

import android.view.View;

import com.startlinesoft.icore.solidario.android.fontigo.enums.TipoRecyclerViewItem;

public interface ICoreRecyclerViewItemListener {

    public void onRecyclerViewItemClick(View v, int posicion, Integer id, TipoRecyclerViewItem tipo);
}
