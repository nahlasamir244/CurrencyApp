package com.nahlasamir244.core.data.sharedpref

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPreferencesRepo @Inject constructor(@ApplicationContext val context: Context) :
    ISharedPreferencesRepo {

    override var baseCurrency: String
        get() = getString(KEY_BASE, DEFAULT_BASE) ?: DEFAULT_BASE
        set(value) {
            setString(KEY_BASE, value)
        }
    override var httpEtag: String
        get() = getString(KEY_ETAG, "") ?: ""
        set(value) {
            setString(KEY_ETAG, value)
        }

    private val sharePref: SharedPreferences
        get() = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)

    private fun getString(key: String?, defaultValue: String?): String? {
        return sharePref.getString(key, defaultValue)
    }

    private fun setString(key: String?, value: String?) {
        val editor = sharePref.edit()
        editor.putString(key, value).apply()
    }


    private fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        return sharePref.getBoolean(key, defaultValue)
    }

    private fun setBoolean(key: String?, value: Boolean) {
        sharePref.edit().putBoolean(key, value).apply()
    }


    companion object {
        const val SHARED_PREFERENCES_FILE = "sharedPreferences"
        const val KEY_BASE = "base"
        const val KEY_ETAG = "etag"
        const val DEFAULT_BASE = "EUR"
    }
}
