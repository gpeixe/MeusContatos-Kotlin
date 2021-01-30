package com.example.contactslist.adapter

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.contactslist.R
import com.example.contactslist.model.Contato

class ContatosAdapter(
    private val contatosList: MutableList<Contato>,
    private val onContatoClickListener: OnContatoClickListener
) : RecyclerView.Adapter<ContatosAdapter.ContatoViewHolder>(){

    inner class ContatoViewHolder(layoutContatoView: View):
            RecyclerView.ViewHolder(layoutContatoView),  View.OnCreateContextMenuListener  {
                val nameTv: TextView = layoutContatoView.findViewById(R.id.nameTv)
                val phoneTv: TextView = layoutContatoView.findViewById((R.id.phoneTv))

        init {
            layoutContatoView.setOnCreateContextMenuListener(this)
        }

        private val POSICAO_INVALIDA = -1
        var posicao: Int = POSICAO_INVALIDA

        override fun onCreateContextMenu(menu: ContextMenu?, view: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            //Criar as opções de menu
            menu?.add("Editar")?.setOnMenuItemClickListener {
                if (posicao != POSICAO_INVALIDA){
                    onContatoClickListener.onUpdateContatoContextMenuClick(posicao)
                    true
                }
                false
            }
            menu?.add("Deletar")?.setOnMenuItemClickListener {
                if (posicao != POSICAO_INVALIDA){
                    onContatoClickListener.onDeleteContatoContextMenuClick(posicao)
                    true
                }
                false
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContatoViewHolder {
        val layoutContatoView: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.layout_contato, parent, false)

        return ContatoViewHolder(layoutContatoView)
    }

    override fun onBindViewHolder(holder: ContatoViewHolder, position: Int) {
        val contato = contatosList[position]
        holder.nameTv.text = contato.nome
        holder.phoneTv.text = contato.telefone

        holder.itemView.setOnClickListener{
            onContatoClickListener.onContatoClick(position)
        }
        holder.posicao = position

    }

    override fun getItemCount(): Int = contatosList.size
}