package com.example.projekuas

import android.content.Context
import android.content.SharedPreferences

class PrefManager private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_FILENAME = "AuthAppPrefs"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USERNAME = "username"
        private const val KEY_ROLE = "role"

        @Volatile
        private var instance: PrefManager? = null

        fun getInstance(context: Context): PrefManager {
            return instance ?: synchronized(this) {
                instance ?: PrefManager(context.applicationContext).also { instance = it }
            }
        }
    }

    // Simpan status login
    fun setLoggedIn(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    // Cek apakah sudah login
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    // Simpan username
    fun saveUsername(username: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USERNAME, username)
        editor.apply()
    }

    // Ambil username
    fun getUsername(): String? {
        return sharedPreferences.getString(KEY_USERNAME, null)
    }

    // Simpan role
    fun saveRole(role: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_ROLE, role)
        editor.apply()
    }

    // Ambil role
    fun getRole(): String? {
        return sharedPreferences.getString(KEY_ROLE, null)
    }

    // Hapus semua data (logout)
    fun clear() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
