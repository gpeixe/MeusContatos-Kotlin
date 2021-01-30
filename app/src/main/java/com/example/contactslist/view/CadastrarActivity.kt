package com.example.contactslist.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.contactslist.databinding.ActivityCadastrarBinding
import com.example.contactslist.services.AutenticadorFirebase

class CadastrarActivity : AppCompatActivity() {
    // View binding
    private lateinit var activityCadastrarBinding: ActivityCadastrarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instanciando view binding
        activityCadastrarBinding = ActivityCadastrarBinding.inflate(layoutInflater)

        setContentView(activityCadastrarBinding.root)
    }

    fun onClick(view: View) {
        if (view == activityCadastrarBinding.cadastrarBt) {
            val email = activityCadastrarBinding.emailEt.text.toString()
            val senha = activityCadastrarBinding.senhaEt.text.toString()
            val repetirSenha = activityCadastrarBinding.repetirSenhaEt.text.toString()

            if (saoValoresValidos(email, senha, repetirSenha)) {
                // Criar usuário no Firebase
                AutenticadorFirebase.auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Usuário $email criado com sucesso.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    else {
                        Toast.makeText(this, "Erro na criação do usuário.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else {
                Toast.makeText(this, "Campos preenchidos incorretamente.", Toast.LENGTH_SHORT).show()
                activityCadastrarBinding.emailEt.requestFocus()
            }
        }
    }

    fun saoValoresValidos(email: String, senha: String, repetirSenha: String): Boolean {
        return if (email.isBlank() || email.isEmpty())
            false
        else if (senha.isBlank() || senha.isEmpty())
            false
        else if (repetirSenha.isBlank() || repetirSenha.isEmpty())
            false
        else senha.equals(repetirSenha)
    }

}