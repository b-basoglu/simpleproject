package com.biometric.biometriclogin.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class PreferenceProviderImpl constructor(context: Context): PreferenceProvider {
    companion object{
        private const val PREF_NAME = "pref-name"
    }

    private val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    private var pref : SharedPreferences = EncryptedSharedPreferences.create(
        context,
        PREF_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private var editor: SharedPreferences.Editor = pref.edit()

    override fun getString(key: String, defaultValue: String): String {
        return pref.getString(key,defaultValue)?:""
    }

    override fun putString(key: String, value: String) {
        editor.putString(key, value).apply()
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return pref.getInt(key,defaultValue)
    }

    override fun putInt(key: String, value: Int) {
        editor.putInt(key, value).apply()
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        return pref.getFloat(key,defaultValue)
    }

    override fun putFloat(key: String, value: Float) {
        editor.putFloat(key, value).apply()
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return pref.getBoolean(key,defaultValue)
    }

    override fun putBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value).apply()
    }

}