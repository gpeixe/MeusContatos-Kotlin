package com.example.contactslist.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.contactslist.R
import com.example.contactslist.databinding.ActivityAutenticacaoBinding
import com.example.contactslist.services.AutenticadorFirebase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

class AutenticacaoActivity : AppCompatActivity() {
    // View binding
    private lateinit var activityAutenticacaoBinding: ActivityAutenticacaoBinding

    // Google Sign In Options e Request Code
    private lateinit var googleSignInOptions: GoogleSignInOptions
    private val GOOGLE_SIGN_IN_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instanciando view binding
        activityAutenticacaoBinding = ActivityAutenticacaoBinding.inflate(layoutInflater)

        setContentView(activityAutenticacaoBinding.root)

        // Instanciando o GSO
        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        // Instanciando o GSC
        AutenticadorFirebase.googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        activityAutenticacaoBinding.loginGoogleBt.setOnClickListener{
            // Verificar se já existe alguém conectado
            val googleSignInAccount: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
            if (googleSignInAccount == null) {
                // Não existe uma conta Google logada ainda
                startActivityForResult(AutenticadorFirebase.googleSignInClient?.signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE)
            }
            else {
                // Já existe uma conta Google logada
                posLoginSucesso()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            // Pegando dados de retorno da intent em um objeto
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Pegando uma conta google a partir do objeto task
                val contaGoogle = task.getResult(ApiException::class.java)
                if (contaGoogle != null) {
                    // Extraindo as credenciais a partir do idToken da conta Google
                    val credencial = GoogleAuthProvider.getCredential(contaGoogle.idToken, null)

                    AutenticadorFirebase.auth.signInWithCredential(credencial).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Usuário ${contaGoogle.email} autenticado com sucesso.", Toast.LENGTH_SHORT).show()
                            posLoginSucesso()
                        }
                        else {
                            Toast.makeText(this, "Falha na autenticação Google.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else {
                    Toast.makeText(this, "Falha na autenticação Google.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Falha na autenticação Google.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onClick(view: View) {
        when(view.id) {
            R.id.cadastrarBt -> {
                startActivity(Intent(this, CadastrarActivity::class.java))
            }
            R.id.entrarBt -> {
                val email = activityAutenticacaoBinding.emailEt.text.toString()
                val senha = activityAutenticacaoBinding.senhaEt.text.toString()
                AutenticadorFirebase.auth.signInWithEmailAndPassword(email, senha).addOnSuccessListener {
                    Toast.makeText(this, "Usuário $email autenticado com sucesso.", Toast.LENGTH_SHORT).show()
                    posLoginSucesso()
                }.addOnFailureListener {
                    Toast.makeText(this, "Falha na autenticação do usuário.", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.recuperarSenhaBt -> {
                startActivity(Intent(this, RecuperarSenhaActivity::class.java))
            }
        }
    }

    private fun posLoginSucesso() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}