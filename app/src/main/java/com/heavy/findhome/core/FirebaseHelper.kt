package com.heavy.findhome.core

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseHelper {

    fun getFirestoreConnection(): FirebaseFirestore {
        val firestore = FirebaseFirestore.getInstance()
        return firestore
    }

    fun getFirebaseAuth(): FirebaseAuth {
        val firebaseAuth = FirebaseAuth.getInstance()
        return firebaseAuth
    }
}