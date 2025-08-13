package com.sortisplus.core.database.dao

import androidx.room.*
import com.sortisplus.core.database.model.PersonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    @Query("SELECT * FROM people ORDER BY lastName ASC, firstName ASC")
    fun observeAll(): Flow<List<PersonEntity>>

    @Query("SELECT * FROM people WHERE id = :id")
    suspend fun getById(id: Long): PersonEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: PersonEntity): Long

    @Update
    suspend fun update(entity: PersonEntity): Int

    @Delete
    suspend fun delete(entity: PersonEntity): Int

    @Query("DELETE FROM people WHERE id = :id")
    suspend fun deleteById(id: Long): Int
}
