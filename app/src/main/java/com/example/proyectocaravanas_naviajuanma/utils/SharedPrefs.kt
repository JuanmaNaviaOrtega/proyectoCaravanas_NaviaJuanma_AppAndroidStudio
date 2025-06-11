package com.example.proyectocaravanas_naviajuanma.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefs(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    fun saveAuthToken(token: String) {
        prefs.edit().putString("token", token).apply()
    }

    fun getAuthToken(): String? {
        return prefs.getString("token", null)
    }

    fun clearAuthToken() {
        prefs.edit().remove("token").apply()
    }
}