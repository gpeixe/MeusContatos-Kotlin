package com.example.contactslist.adapter


interface OnContatoClickListener {
    fun onContatoClick(position: Int)
    fun onDeleteContatoContextMenuClick(position: Int)
    fun onUpdateContatoContextMenuClick(position: Int)
}