package com.sortisplus.shared.domain.model

/**
 * Domain validation result wrapper for business rule validation
 */
data class ValidationResult(
    val isValid: Boolean,
    val errors: List<String> = emptyList()
) {
    val errorMessage: String?
        get() = if (errors.isNotEmpty()) errors.joinToString(", ") else null
    
    companion object {
        fun success() = ValidationResult(true)
        fun failure(vararg errors: String) = ValidationResult(false, errors.toList())
        fun failure(message: String) = ValidationResult(false, listOf(message))
    }
}