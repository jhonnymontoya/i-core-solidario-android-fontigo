package com.startlinesoft.icore.solidario.android.ais;

import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.startlinesoft.icore.solidario.android.ais.utilidades.ICoreConstantes;

public class ConfiguracionFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        this.setPreferencesFromResource(R.xml.configuraciones, rootKey);

        Preference touchIdPreference = this.findPreference(ICoreConstantes.PREFERENCE_TOUCHID);
        touchIdPreference.setOnPreferenceChangeListener(this);
        touchIdPreference.setEnabled(false);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Toast.makeText(this.getContext(), newValue.toString(), Toast.LENGTH_SHORT).show();
        return false;
    }
}
