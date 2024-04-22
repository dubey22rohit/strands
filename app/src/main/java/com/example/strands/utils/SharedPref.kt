package com.example.strands.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE

object SharedPref {
    fun storeData(
        username: String,
        email: String,
        bio: String? = "",
        imageUrl: String,
        context: Context
    ) {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("username", username)
        editor.putString("email", email)
        editor.putString("bio", bio)
        editor.putString("imageUrl", imageUrl)
        editor.apply()
    }

    fun getUsername(context: Context):String{
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("username", "")!!
    }

    fun getEmail(context: Context):String{
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("email", "")!!
    }

    fun getBio(context: Context):String{
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("bio", "")!!
    }

    fun getImageUrl(context: Context):String{
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("imageUrl", "")!!
    }
}