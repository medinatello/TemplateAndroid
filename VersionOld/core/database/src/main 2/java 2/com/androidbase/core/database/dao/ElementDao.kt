package com.androidbase.core.database.dao

import androidx.room.*
import com.androidbase.core.database.model.ElementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ElementDao {

    @Query("SELECT * FROM elements ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<ElementEntity>>

    @Query("SELECT * FROM elements WHERE id = :id")
    suspend fun getById(id: Long): ElementEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ElementEntity): Long

    @Update
    suspend fun update(entity: ElementEntity): Int

    @Delete
    suspend fun delete(entity: ElementEntity): Int

    @Query("DELETE FROM elements WHERE id = :id")
    suspend fun deleteById(id: Long): Int
}