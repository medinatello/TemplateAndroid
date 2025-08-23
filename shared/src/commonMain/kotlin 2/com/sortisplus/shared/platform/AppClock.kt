package com.sortisplus.shared.platform

expect interface AppClock {
    fun now(): Long
    fun currentTimeMillis(): Long
    fun formatDateTime(timestamp: Long, pattern: String): String
}