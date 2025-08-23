package com.sortisplus.shared.domain.model

/**
 * Domain model for Element entity
 */
data class Element(
    val id: Long,
    val title: String,
    val createdAt: Long,
    val updatedAt: Long? = null
)