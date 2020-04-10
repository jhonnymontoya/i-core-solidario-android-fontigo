package com.startlinesoft.icore.solidario.android.ais

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.startlinesoft.icore.solidario.android.ais.databinding.ActivityOlvideClaveBinding

class OlvideClaveActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var bnd: ActivityOlvideClaveBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bnd = ActivityOlvideClaveBinding.inflate(layoutInflater)
        setContentView(bnd.root)

        val usuario = intent.getStringExtra("USUARIO")
        bnd.etUser.setText(usuario)

        bnd.btnSubmit.setOnClickListener(this)
        bnd.btnSubmit.isEnabled = usuario.length > 0

        bnd.tvVolver.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if(v.equals(bnd.btnSubmit)) {
            //TODO: Implementar la lógica para que envie el correo de restauación de contraseña
        }
        finish()
    }
}
