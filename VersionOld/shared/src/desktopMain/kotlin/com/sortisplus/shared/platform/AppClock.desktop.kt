package com.sortisplus.shared.platform

import java.text.SimpleDateFormat
import java.util.*

actual interface AppClock {
    actual fun now(): Long
    actual fun currentTimeMillis(): Long
    actual fun formatDateTime(timestamp: Long, pattern: String): String
}

class DesktopAppClock : AppClock {
    
    override fun now(): Long = System.currentTimeMillis()
    
    override fun currentTimeMillis(): Long = System.currentTimeMillis()
    
    override fun formatDateTime(timestamp: Long, pattern: String): String {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        return formatter.format(Date(timestamp))
    }
}