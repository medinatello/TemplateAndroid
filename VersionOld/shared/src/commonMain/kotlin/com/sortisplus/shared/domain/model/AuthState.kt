package com.sortisplus.shared.domain.model

/**
 * Represents the different authentication states in the application
 */
sealed class AuthState {
    /**
     * User is not authenticated
     */
    object Unauthenticated : AuthState()
    
    /**
     * Authentication process is ongoing
     */
    object Loading : AuthState()
    
    /**
     * User is successfully authenticated
     * @param userInfo Basic user information
     */
    data class Authenticated(
        val userInfo: UserInfo
    ) : AuthState()
    
    /**
     * Authentication failed
     * @param error Error message describing the failure
     */
    data class Error(
        val error: String
    ) : AuthState()
}

/**
 * Basic user information for authenticated users
 */
data class UserInfo(
    val email: String,
    val name: String? = null,
    val userId: String? = null
) {
    companion object {
        /**
         * Creates a UserInfo instance from email for simple authentication
         */
        fun fromEmail(email: String): UserInfo {
            return UserInfo(
                email = email,
                name = email.substringBefore("@").replaceFirstChar { it.uppercase() },
                userId = email.hashCode().toString()
            )
        }
    }
}