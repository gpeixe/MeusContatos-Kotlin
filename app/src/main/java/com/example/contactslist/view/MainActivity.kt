package com.example.contactslist.view

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactslist.R
import com.example.contactslist.adapter.ContatosAdapter
import com.example.contactslist.adapter.OnContatoClickListener
import com.example.contactslist.controller.ContatoController
import com.example.contactslist.databinding.ActivityMainBinding
import com.example.contactslist.model.Contato
import com.example.contactslist.view.MainActivity.Extras.EXTRA_CONTATO
import com.example.contactslist.view.MainActivity.Extras.VISUALIZAR_CONTATO_ACTION

class MainActivity : AppCompatActivity(), OnContatoClickListener {
    private lateinit var contatosList: MutableList<Contato>
    private lateinit var contatosLayoutManager: LinearLayoutManager;
    private lateinit var contatosAdapter: ContatosAdapter;
    private lateinit var activityMainBinding: ActivityMainBinding

    private lateinit var contatoController: ContatoController


    private val NOVO_CONTATO_REQUEST_CODE = 0
    private val EDITAR_CONTATO_REQUEST_CODE = 1

    object Extras {
        val EXTRA_CONTATO = "EXTRA_CONTATO"
        val VISUALIZAR_CONTATO_ACTION = "VISUALIZAR_CONTATO_ACTION"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        // Instanciamento Controller
        contatoController = ContatoController(this)

        contatosList = mutableListOf()
        val populaContatosListAt = object: AsyncTask<Void, Void, List<Contato>>() {
            override fun doInBackground(vararg p0: Void?): List<Contato> {
                // Thread filha
                Thread.sleep(5000)
                return contatoController.buscaContatos()
            }

            override fun onPreExecute() {
                super.onPreExecute()
                // Thread de GUI
                activityMainBinding.contatosListPb.visibility = View.VISIBLE
                activityMainBinding.listaContatosRv.visibility = View.GONE
            }

            override fun onPostExecute(result: List<Contato>?) {
                super.onPostExecute(result)
                // Thread de GUI
                activityMainBinding.contatosListPb.visibility = View.GONE
                activityMainBinding.listaContatosRv.visibility = View.VISIBLE
                if (result != null) {
                    contatosList.clear()
                    contatosList.addAll(result)
                    contatosAdapter.notifyDataSetChanged()
                }
            }
        }
        populaContatosListAt.execute()

        contatosLayoutManager = LinearLayoutManager(this)
        contatosAdapter = ContatosAdapter(contatosList, this)
        activityMainBinding.listaContatosRv.adapter = contatosAdapter
        activityMainBinding.listaContatosRv.layoutManager = contatosLayoutManager
    }

    override fun onContatoClick(position: Int) {
        val contato: Contato = contatosList[position]
        val visualizarContatoIntent = Intent(this, ContatoActivity::class.java)
        visualizarContatoIntent.putExtra(EXTRA_CONTATO, contato)
        visualizarContatoIntent.action = VISUALIZAR_CONTATO_ACTION
        startActivity(visualizarContatoIntent)

    }

    override fun onDeleteContatoContextMenuClick(position: Int) {
        val contato: Contato = contatosList[position]
        contatoController.removeContato(contato.nome)
        contatosList.removeAt(position)
        contatosAdapter.notifyDataSetChanged()

    }

    override fun onUpdateContatoContextMenuClick(position: Int) {
        val contato: Contato = contatosList[position]
        val editarContantoIntent = Intent(this, ContatoActivity::class.java)
        editarContantoIntent.putExtra(EXTRA_CONTATO, contato)
        startActivityForResult(editarContantoIntent, EDITAR_CONTATO_REQUEST_CODE)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            if (item.itemId == R.id.novoContatoMi) {
                val novoContantoIntent = Intent(this, ContatoActivity::class.java)
                startActivityForResult(novoContantoIntent, NOVO_CONTATO_REQUEST_CODE)
                true
            }
            else
                false

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NOVO_CONTATO_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            val novoContato = data.getParcelableExtra<Contato>(EXTRA_CONTATO)
            if(novoContato != null){
                contatoController.insereContato(novoContato)
                contatosList.add(novoContato)
                contatosAdapter.notifyDataSetChanged()
            }
        }
        else if(requestCode == EDITAR_CONTATO_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            val contatoAtualizado = data.getParcelableExtra<Contato>(EXTRA_CONTATO)
            if(contatoAtualizado != null) {
                contatoController.atualizaContato(contatoAtualizado)
                val posicao = contatosList.indexOfFirst { it.nome.equals(contatoAtualizado.nome) }
                contatosList[posicao] = contatoAtualizado
                contatosAdapter.notifyDataSetChanged()

            }
        }
    }
}