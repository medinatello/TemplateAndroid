package com.sortisplus.shared.domain.repository

import com.sortisplus.shared.domain.model.Element
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Element operations
 */
interface ElementRepository {
    fun observeAll(): Flow<List<Element>>
    suspend fun create(title: String): Long
    suspend fun update(id: Long, title: String): Boolean
    suspend fun delete(id: Long): Boolean
}