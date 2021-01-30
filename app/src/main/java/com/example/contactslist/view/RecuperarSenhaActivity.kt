package com.example.contactslist.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.contactslist.databinding.ActivityCadastrarBinding
import com.example.contactslist.databinding.ActivityRecuperarSenhaBinding
import com.example.contactslist.services.AutenticadorFirebase

class RecuperarSenhaActivity : AppCompatActivity() {
    // View binding
    private lateinit var binding: ActivityRecuperarSenhaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instanciando view binding
        binding = ActivityRecuperarSenhaBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }

    fun onClick(view: View) {
        if (view == binding.enviarEmailBt) {
            val email = binding.emailRecuperacaoSenhaEt.text.toString()
            if (email.isNotBlank() && email.isNotEmpty()) {
                AutenticadorFirebase.auth.sendPasswordResetEmail(email)
                Toast.makeText(this, "E-mail de recuperação de senha enviado para $email.", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }
}