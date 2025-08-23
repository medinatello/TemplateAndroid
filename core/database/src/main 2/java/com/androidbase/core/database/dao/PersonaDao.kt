package com.androidbase.core.database.dao

import androidx.room.*
import com.androidbase.core.database.model.PersonaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonaDao {
    @Query("SELECT * FROM personas ORDER BY apellido ASC, nombre ASC")
    fun observeAll(): Flow<List<PersonaEntity>>

    @Query("SELECT * FROM personas WHERE id = :id")
    suspend fun getById(id: Long): PersonaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: PersonaEntity): Long

    @Update
    suspend fun update(entity: PersonaEntity): Int

    @Delete
    suspend fun delete(entity: PersonaEntity): Int

    @Query("DELETE FROM personas WHERE id = :id")
    suspend fun deleteById(id: Long): Int
}