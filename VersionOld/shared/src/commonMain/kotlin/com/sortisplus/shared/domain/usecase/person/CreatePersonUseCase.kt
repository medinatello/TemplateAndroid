package com.sortisplus.shared.domain.usecase.person

import com.sortisplus.shared.domain.model.DatabaseResult
import com.sortisplus.shared.domain.model.Person
import com.sortisplus.shared.domain.repository.PersonRepository
import com.sortisplus.shared.platform.AppClock

/**
 * Use case for creating a new person with validation
 */
class CreatePersonUseCase(
    private val personRepository: PersonRepository,
    private val appClock: AppClock
) {
    suspend fun execute(
        firstName: String,
        lastName: String,
        birthDateMillis: Long,
        weightKg: Double,
        isLeftHanded: Boolean
    ): DatabaseResult<Long> {
        // Validate input data using current time from AppClock
        val validationResult = Person.validate(
            firstName, 
            lastName, 
            birthDateMillis, 
            weightKg, 
            appClock.currentTimeMillis()
        )
        if (!validationResult.isValid) {
            return DatabaseResult.Error(
                IllegalArgumentException("Validation failed: ${validationResult.errorMessage}")
            )
        }
        
        // Create person through repository
        return personRepository.create(firstName, lastName, birthDateMillis, weightKg, isLeftHanded)
    }
}