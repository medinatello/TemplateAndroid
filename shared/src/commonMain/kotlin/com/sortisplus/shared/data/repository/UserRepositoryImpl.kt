package com.sortisplus.shared.data.repository

import com.sortisplus.shared.data.cache.CacheService
import com.sortisplus.shared.database.AppDatabase
import com.sortisplus.shared.domain.model.User
import com.sortisplus.shared.domain.repository.UserRepository
import com.sortisplus.shared.platform.AppClock
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlin.time.Duration.Companion.hours

class UserRepositoryImpl(
    private val httpClient: HttpClient,
    private val database: AppDatabase,
    private val cacheService: CacheService,
    private val appClock: AppClock
) : UserRepository {
    
    private val baseUrl = "https://jsonplaceholder.typicode.com"
    
    override suspend fun getAllUsers(): List<User> = withContext(Dispatchers.Default) {
        val cacheKey = "users_all"
        
        // Try to get from cache first
        val cachedData = cacheService.get(cacheKey)
        if (cachedData != null) {
            return@withContext Json.decodeFromString<List<User>>(cachedData)
        }
        
        // If not in cache, fetch from network
        try {
            val response = httpClient.get("$baseUrl/users")
            if (response.status == HttpStatusCode.OK) {
                val users: List<User> = response.body()
                
                // Cache the response
                cacheService.put(cacheKey, Json.encodeToString(users), ttl = 1.hours)
                
                // Also store in local database
                users.forEach { user ->
                    database.appDatabaseQueries.insertUser(
                        name = user.name,
                        email = user.email,
                        createdAt = user.createdAt
                    )
                }
                
                return@withContext users
            }
        } catch (e: Exception) {
            // On network error, try to get from local database as fallback
            return@withContext getUsersFromDatabase()
        }
        
        // Final fallback to database
        return@withContext getUsersFromDatabase()
    }
    
    override suspend fun getUserById(id: Long): User? = withContext(Dispatchers.Default) {
        val cacheKey = "user_$id"
        
        // Try cache first
        val cachedData = cacheService.get(cacheKey)
        if (cachedData != null) {
            return@withContext Json.decodeFromString<User>(cachedData)
        }
        
        // Try network
        try {
            val response = httpClient.get("$baseUrl/users/$id")
            if (response.status == HttpStatusCode.OK) {
                val user: User = response.body()
                
                // Cache the response
                cacheService.put(cacheKey, Json.encodeToString(user), ttl = 1.hours)
                
                return@withContext user
            }
        } catch (e: Exception) {
            // Fallback to database
        }
        
        // Fallback to database
        val localUser = database.appDatabaseQueries.selectById(id).executeAsOneOrNull()
        return@withContext localUser?.let { 
            User(
                id = it.id,
                name = it.name,
                email = it.email,
                createdAt = it.createdAt
            )
        }
    }
    
    override suspend fun createUser(name: String, email: String): User = withContext(Dispatchers.Default) {
        val currentTime = appClock.currentTimeMillis()
        
        // For demo purposes, create locally first
        database.appDatabaseQueries.insertUser(name, email, currentTime)
        val id = database.appDatabaseQueries.lastInsertRowId().executeAsOne()
        
        val user = User(
            id = id,
            name = name,
            email = email,
            createdAt = currentTime
        )
        
        // Invalidate cache
        cacheService.delete("users_all")
        
        return@withContext user
    }
    
    override suspend fun updateUser(user: User): User = withContext(Dispatchers.Default) {
        database.appDatabaseQueries.updateUser(user.name, user.email, user.id)
        
        // Invalidate cache
        cacheService.delete("users_all")
        cacheService.delete("user_${user.id}")
        
        return@withContext user
    }
    
    override suspend fun deleteUser(id: Long) = withContext(Dispatchers.Default) {
        database.appDatabaseQueries.deleteUser(id)
        
        // Invalidate cache
        cacheService.delete("users_all")
        cacheService.delete("user_$id")
    }
    
    override fun observeUsers(): Flow<List<User>> = flow {
        emit(getAllUsers())
    }
    
    private fun getUsersFromDatabase(): List<User> {
        return database.appDatabaseQueries.selectAll().executeAsList().map { 
            User(
                id = it.id,
                name = it.name,
                email = it.email,
                createdAt = it.createdAt
            )
        }
    }
}