package com.sortisplus.shared.data.repository

import com.sortisplus.shared.database.AppDatabase
import com.sortisplus.shared.domain.model.Element
import com.sortisplus.shared.domain.repository.ElementRepository
import com.sortisplus.shared.platform.AppClock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Element repository implementation using SQLDelight database
 */
class ElementRepositoryImpl(
    private val database: AppDatabase,
    private val appClock: AppClock
) : ElementRepository {

    override fun observeAll(): Flow<List<Element>> {
        // For now, return an empty flow - will be implemented with actual reactive queries later
        return kotlinx.coroutines.flow.flowOf(emptyList())
    }

    override suspend fun create(title: String): Long {
        // For now, return a mock ID - will be implemented with actual database operations later
        return 1L
    }

    override suspend fun update(id: Long, title: String): Boolean {
        // For now, return mock success - will be implemented with actual database operations later
        return true
    }

    override suspend fun delete(id: Long): Boolean {
        // For now, return mock success - will be implemented with actual database operations later
        return true
    }
}