package com.example.contactslist.controller

import com.example.contactslist.data.ContatoDao
import com.example.contactslist.data.ContatoFirebase
import com.example.contactslist.data.ContatoSqlite
import com.example.contactslist.model.Contato
import com.example.contactslist.view.MainActivity

class ContatoController(mainActivity: MainActivity)  {
    val contatoDao: ContatoDao
    init {
        //contatoDao = ContatoSqlite(mainActivity)
        contatoDao = ContatoFirebase()
    }

    fun insereContato(contato: Contato) = contatoDao.createContato(contato)
    fun buscaContato(nome: String) = contatoDao.readContato(nome)
    fun buscaContatos() = contatoDao.readContatos()
    fun atualizaContato(contato: Contato) = contatoDao.updateContato(contato)
    fun removeContato(nome: String) = contatoDao.deleteContato(nome)
}