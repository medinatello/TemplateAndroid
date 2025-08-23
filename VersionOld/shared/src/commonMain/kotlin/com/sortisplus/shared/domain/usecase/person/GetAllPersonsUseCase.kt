package com.sortisplus.shared.domain.usecase.person

import com.sortisplus.shared.domain.model.Person
import com.sortisplus.shared.domain.repository.PersonRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for observing all persons with reactive updates
 */
class GetAllPersonsUseCase(
    private val personRepository: PersonRepository
) {
    fun execute(): Flow<List<Person>> {
        return personRepository.observeAll()
    }
}