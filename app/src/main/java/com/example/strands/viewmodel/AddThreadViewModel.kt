package com.example.strands.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.strands.model.StringModel
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

class AddThreadViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    private var threadRef = db.getReference("threads")

    private val storageRef = Firebase.storage.reference
    private val imageRef = storageRef.child("threads/${UUID.randomUUID()}.jpg")

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> = _isPosted


    fun saveImage(
        stringText: String,
        userId: String,
        imageUri: Uri?,
    ) {
        imageUri?.let { imageRef.putFile(imageUri) }?.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveData(stringText, userId, it.toString())
            }
        }
    }

    fun saveData(
        stringText: String,
        userId: String,
        imageUri: String
    ) {
        val threadData =
            StringModel(stringText, imageUri, userId, System.currentTimeMillis().toString())
        threadRef.child(threadRef.push().key!!).setValue(threadData)
            .addOnSuccessListener {
                _isPosted.postValue(true)
            }
            .addOnFailureListener {
                _isPosted.postValue(false)
            }
    }
}