package com.example.strands.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.strands.model.UserModel
import com.example.strands.utils.SharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class AuthViewModel : ViewModel() {
    val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    var userRef = db.getReference("users")

    private val storageRef = Firebase.storage.reference
    private val imageRef = storageRef.child("users/${UUID.randomUUID()}.jpg")

    private val _firebaseUser = MutableLiveData<FirebaseUser>()
    val firebaseUser: LiveData<FirebaseUser> = _firebaseUser

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        _firebaseUser.value = auth.currentUser
    }

    fun login(email: String, password: String, context: Context) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                _firebaseUser.postValue(auth.currentUser)
                getData(auth.currentUser?.uid, context)
            } else {
                _error.postValue(it.exception?.message)
            }
        }
    }

    private fun getData(uid: String?, context: Context) {
        userRef.child(uid!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(UserModel::class.java)
                SharedPref.storeData(
                    userData!!.username,
                    userData.email,
                    userData.bio,
                    userData.imageUrl,
                    context
                )
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun register(
        username: String,
        email: String,
        password: String,
        bio: String?,
        imageUri: Uri?,
        context: Context
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                _firebaseUser.postValue(auth.currentUser)
                saveImage(username, email, password, bio, imageUri, auth.currentUser?.uid, context)
            } else {
                _error.postValue(it.exception?.message)
            }
        }
    }

    private fun saveImage(
        username: String,
        email: String,
        password: String,
        bio: String?,
        imageUri: Uri?,
        uid: String?,
        context: Context
    ) {
        imageUri?.let { imageRef.putFile(imageUri) }?.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveData(username, email, password, bio, it.toString(), uid!!, context)
            }
        }
    }

    private fun saveData(
        username: String,
        email: String,
        password: String,
        bio: String?,
        imageUrl: String,
        uid: String?,
        context: Context
    ) {
        val userData = UserModel(username, email, password, bio, imageUrl, uid!!)
        userRef.child(uid).setValue(userData)
            .addOnSuccessListener {
                SharedPref.storeData(username, email, bio, imageUrl, context)
            }
            .addOnFailureListener { }
    }

    fun logout() {
        auth.signOut()
        _firebaseUser.postValue(null)
    }
}