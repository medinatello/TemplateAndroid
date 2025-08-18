package com.sortisplus.shared.domain.usecase.person

import com.sortisplus.shared.domain.model.DatabaseResult
import com.sortisplus.shared.domain.repository.PersonRepository
import com.sortisplus.shared.platform.AppClock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CreatePersonUseCaseTest {

    private val mockAppClock = object : AppClock {
        override fun currentTimeMillis(): Long = 1640995200000L // 2022-01-01
        override fun now(): Long = 1640995200000L // Same as currentTimeMillis
        override fun formatDateTime(timestamp: Long, pattern: String): String = "2022-01-01"
    }

    private val mockPersonRepository = object : PersonRepository {
        override fun observeAll() = TODO("Not implemented for test")
        override suspend fun getById(id: Long) = TODO("Not implemented for test")
        override suspend fun create(
            firstName: String,
            lastName: String,
            birthDateMillis: Long,
            weightKg: Double,
            isLeftHanded: Boolean
        ): DatabaseResult<Long> {
            return DatabaseResult.Success(1L)
        }
        override suspend fun update(
            id: Long,
            firstName: String,
            lastName: String,
            birthDateMillis: Long,
            weightKg: Double,
            isLeftHanded: Boolean
        ) = TODO("Not implemented for test")
        override suspend fun delete(id: Long) = TODO("Not implemented for test")
    }

    private val createPersonUseCase = CreatePersonUseCase(mockPersonRepository, mockAppClock)

    @Test
    fun `execute with valid data should return success`() = runTest {
        val result = createPersonUseCase.execute(
            firstName = "John",
            lastName = "Doe",
            birthDateMillis = 1546300800000L, // 2019-01-01 - 3 years before mock current time
            weightKg = 70.0,
            isLeftHanded = false
        )

        assertTrue(result is DatabaseResult.Success)
        assertEquals(1L, (result as DatabaseResult.Success).data)
    }

    @Test
    fun `execute with invalid data should return error`() = runTest {
        val result = createPersonUseCase.execute(
            firstName = "", // Invalid empty name
            lastName = "Doe",
            birthDateMillis = 1546300800000L,
            weightKg = 70.0,
            isLeftHanded = false
        )

        assertTrue(result is DatabaseResult.Error)
        assertTrue((result as DatabaseResult.Error).exception.message?.contains("Validation failed") == true)
    }
}