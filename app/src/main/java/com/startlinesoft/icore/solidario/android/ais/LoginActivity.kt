package com.startlinesoft.icore.solidario.android.ais

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.startlinesoft.icore.solidario.android.ais.databinding.LoginMainBinding

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var bnd: LoginMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bnd = LoginMainBinding.inflate(layoutInflater)
        setContentView(bnd.root)

        bnd.etUser.addTextChangedListener(textWatcher)
        bnd.etPassword.addTextChangedListener(textWatcher)
        bnd.btnLogin.setOnClickListener(this)
        bnd.tvForgotPassword.setOnClickListener(this)
    }

    val textWatcher: TextWatcher
        get() = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val user = bnd.etUser.text.toString().trim { it <= ' ' }.length
                val password = bnd.etPassword.text.toString().trim { it <= ' ' }.length
                bnd.btnLogin.isEnabled = user > 0 && password > 0
            }
            override fun afterTextChanged(s: Editable) {}
        }

    override fun onClick(v: View) {
        if (v == bnd.btnLogin) {
            Toast.makeText(this, "Click 3d", Toast.LENGTH_SHORT).show()
            bnd.tvErrror.visibility = View.VISIBLE
        }
        if (v == bnd.tvForgotPassword) {
            bnd.tvErrror.visibility = View.GONE
            val usuario = bnd.etUser.text.toString().trim { it <= ' ' }
            val i = Intent(applicationContext, OlvideClaveActivity::class.java).apply {
                putExtra("USUARIO", usuario)
            }
            startActivity(i)
        }
    }
}