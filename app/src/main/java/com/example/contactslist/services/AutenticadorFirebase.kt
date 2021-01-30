package com.example.contactslist.services

import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignInClient

object AutenticadorFirebase {
    var auth = FirebaseAuth.getInstance()
    var googleSignInClient: GoogleSignInClient? = null

}