package com.example.doggydatingapp

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*

object FirebaseInfo {

    private val firestore = FirebaseFirestore.getInstance()

    fun saveUserBasic(uid: String, email: String): Task<Void> {
        val data = mapOf("uid" to uid, "email" to email)
        return firestore.collection("users").document(uid).set(data)
    }

    fun getUserData(uid: String): Task<DocumentSnapshot> {
        return firestore.collection("users").document(uid).get()
    }

}