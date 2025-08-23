package com.sortisplus.shared.platform

import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

actual interface KeyValueStore {
    actual fun getString(key: String): String?
    actual fun putString(key: String, value: String)
    actual fun getInt(key: String, defaultValue: Int): Int
    actual fun putInt(key: String, value: Int)
    actual fun getBoolean(key: String, defaultValue: Boolean): Boolean
    actual fun putBoolean(key: String, value: Boolean)
    actual fun remove(key: String)
    actual fun clear()
}

class AndroidKeyValueStore(private val settings: Settings) : KeyValueStore {
    
    override fun getString(key: String): String? = settings.getStringOrNull(key)
    
    override fun putString(key: String, value: String) {
        settings.putString(key, value)
    }
    
    override fun getInt(key: String, defaultValue: Int): Int = settings.getInt(key, defaultValue)
    
    override fun putInt(key: String, value: Int) {
        settings.putInt(key, value)
    }
    
    override fun getBoolean(key: String, defaultValue: Boolean): Boolean = settings.getBoolean(key, defaultValue)
    
    override fun putBoolean(key: String, value: Boolean) {
        settings.putBoolean(key, value)
    }
    
    override fun remove(key: String) {
        settings.remove(key)
    }
    
    override fun clear() {
        settings.clear()
    }
}