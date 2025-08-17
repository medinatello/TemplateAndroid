package com.sortisplus.shared.platform

expect interface KeyValueStore {
    fun getString(key: String): String?
    fun putString(key: String, value: String)
    fun getInt(key: String, defaultValue: Int = 0): Int
    fun putInt(key: String, value: Int)
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean
    fun putBoolean(key: String, value: Boolean)
    fun remove(key: String)
    fun clear()
}