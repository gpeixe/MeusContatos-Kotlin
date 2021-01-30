package com.example.contactslist.data

import com.example.contactslist.model.Contato

interface ContatoDao {
    fun createContato(contato: Contato)
    fun readContato(nome: String): Contato
    fun readContatos(): MutableList<Contato>
    fun updateContato(contato: Contato)
    fun deleteContato(nome: String)
}