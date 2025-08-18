package com.sortisplus.shared.domain.model

/**
 * Result wrapper for database operations with error handling
 */
sealed class DatabaseResult<out T> {
    data class Success<T>(val data: T) : DatabaseResult<T>()
    data class Error(val exception: Exception) : DatabaseResult<Nothing>()
    
    inline fun <R> map(transform: (value: T) -> R): DatabaseResult<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Error -> Error(exception)
        }
    }
    
    inline fun onSuccess(action: (value: T) -> Unit): DatabaseResult<T> {
        if (this is Success) action(data)
        return this
    }
    
    inline fun onError(action: (exception: Exception) -> Unit): DatabaseResult<T> {
        if (this is Error) action(exception)
        return this
    }
}