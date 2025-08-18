package com.sortisplus.shared.domain.model

/**
 * Domain model for Person entity
 */
data class Person(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val birthDateMillis: Long,
    val weightKg: Double,
    val isLeftHanded: Boolean
) {
    
    /**
     * Calculates age based on provided current time
     */
    fun calculateAge(currentTimeMillis: Long): Int {
        val ageInMillis = currentTimeMillis - birthDateMillis
        val ageInYears = ageInMillis / (365.25 * 24 * 60 * 60 * 1000)
        return ageInYears.toInt()
    }

    companion object {
        /**
         * Validates person data according to business rules
         */
        fun validate(
            firstName: String,
            lastName: String,
            birthDateMillis: Long,
            weightKg: Double,
            currentTimeMillis: Long
        ): ValidationResult {
            val errors = mutableListOf<String>()

            if (firstName.isBlank()) errors.add("First name cannot be empty")
            if (firstName.length > 50) errors.add("First name cannot have more than 50 characters")

            if (lastName.isBlank()) errors.add("Last name cannot be empty")
            if (lastName.length > 50) errors.add("Last name cannot have more than 50 characters")

            if (birthDateMillis <= 0) errors.add("Birth date must be valid")
            if (birthDateMillis > currentTimeMillis) errors.add("Birth date cannot be in the future")

            if (weightKg <= 0) errors.add("Weight must be greater than 0")
            if (weightKg > 1000) errors.add("Weight must be less than 1000kg")

            return if (errors.isEmpty()) {
                ValidationResult.success()
            } else {
                ValidationResult.failure(*errors.toTypedArray())
            }
        }
    }
}