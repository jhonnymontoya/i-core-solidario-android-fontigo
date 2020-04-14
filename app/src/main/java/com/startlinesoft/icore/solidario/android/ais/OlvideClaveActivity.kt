package com.startlinesoft.icore.solidario.android.ais

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
        bnd.etUser.addTextChangedListener(textWatcher)

        bnd.btnSubmit.setOnClickListener(this)
        bnd.btnSubmit.isEnabled = usuario.length > 0

        bnd.tvVolver.setOnClickListener(this)
    }

    val textWatcher: TextWatcher
        get() = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val user = bnd.etUser.text.toString().trim { it <= ' ' }.length
                bnd.btnSubmit.isEnabled = user > 0
            }
            override fun afterTextChanged(s: Editable) {}
        }

    override fun onClick(v: View) {
        if(v == bnd.btnSubmit) {

            bnd.progressBar.visibility = View.VISIBLE
            Thread(object : Runnable {
                var progressStatus = 0
                override fun run() {
                    while (progressStatus < 50) {
                        progressStatus += 1
                        try {
                            Thread.sleep(100)
                        } catch (e: InterruptedException) {
                        }
                    }
                    bnd.progressBar.post(object: Runnable {
                        override fun run() {
                            bnd.progressBar.visibility = View.GONE
                            finish()
                        }
                    })
                }
            }).start()
        }
        if(v == bnd.tvVolver) {
            finish()
        }
    }
}
