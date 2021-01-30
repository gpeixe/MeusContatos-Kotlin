package com.example.contactslist.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.contactslist.R
import com.example.contactslist.databinding.ActivityContatoBinding
import com.example.contactslist.model.Contato

class ContatoActivity : AppCompatActivity() {
    private lateinit var activityContatoBinding: ActivityContatoBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contato)
        activityContatoBinding =  ActivityContatoBinding.inflate(layoutInflater)
        setContentView(activityContatoBinding.root)

        val contato: Contato? = intent.getParcelableExtra(MainActivity.Extras.EXTRA_CONTATO)
        if(contato != null){
            activityContatoBinding.nomeContatoEt.setText(contato.nome)
            activityContatoBinding.nomeContatoEt.isEnabled = false
            activityContatoBinding.emailContatoEt.setText(contato.email)
            activityContatoBinding.telefoneContatoEt.setText(contato.telefone)

            if(intent.action == MainActivity.Extras.VISUALIZAR_CONTATO_ACTION){
                activityContatoBinding.emailContatoEt.isEnabled = false
                activityContatoBinding.telefoneContatoEt.isEnabled = false
                activityContatoBinding.salvarBt.visibility = View.GONE
            }
        }

        activityContatoBinding.salvarBt.setOnClickListener{
            val novoContato = Contato(
                    activityContatoBinding.nomeContatoEt.text.toString(),
                    activityContatoBinding.telefoneContatoEt.text.toString(),
                    activityContatoBinding.emailContatoEt.text.toString()
            )

            val retornoIntent = Intent()
            retornoIntent.putExtra(MainActivity.Extras.EXTRA_CONTATO, novoContato)
            setResult(RESULT_OK, retornoIntent)
            finish()
        }
    }


}