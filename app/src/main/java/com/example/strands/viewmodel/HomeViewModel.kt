package com.example.strands.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.strands.model.StrandModel
import com.example.strands.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeViewModel : ViewModel() {
    private val db = FirebaseDatabase.getInstance()
    val thread = db.getReference("threads")

    private val _threadsAndUsers = MutableLiveData<List<Pair<StrandModel, UserModel>>>()
    val strandsAndUsers: LiveData<List<Pair<StrandModel, UserModel>>> = _threadsAndUsers

    init {
        fetchStrandsAndUsers {
            _threadsAndUsers.value = it
        }
    }

    private fun fetchStrandsAndUsers(onResult: (List<Pair<StrandModel, UserModel>>) -> Unit) {
        thread.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableListOf<Pair<StrandModel, UserModel>>()
                for (strandSnapshot in snapshot.children) {
                    val strand = strandSnapshot.getValue(StrandModel::class.java)
                    strand.let {
                        fetchUserFromStrand(it!!) { user ->
                            result.add(0, it to user)
                            if (result.size == snapshot.childrenCount.toInt()) {
                                onResult(result)
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun fetchUserFromStrand(strand: StrandModel, onResult: (UserModel) -> Unit) {
        db.getReference("users").child(strand.userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java)
                    user?.let(onResult)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

}