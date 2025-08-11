package com.sortisplus.core.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for Person domain model
 * 
 * Tests the business logic, validation, and calculated properties
 * of the Person class to ensure data integrity and correct behavior.
 */
class PersonTest {

    @Test
    fun `when person created, should calculate age correctly`() {
        // Given - Person born 25 years ago
        val currentTime = System.currentTimeMillis()
        val birthDate = currentTime - (25L * 365 * 24 * 60 * 60 * 1000)
        
        val person = Person(
            id = 1L,
            firstName = "John",
            lastName = "Doe",
            birthDateMillis = birthDate,
            weightKg = 70.5,
            isLeftHanded = false
        )
        
        // When calculating age
        val age = person.age
        
        // Then age should be approximately 25 (allowing for leap years)
        assertTrue("Age should be around 25", age >= 24 && age <= 25)
    }

    @Test
    fun `when validating valid person data, should return success`() {
        // Given valid person data
        val currentTime = System.currentTimeMillis()
        val birthDate = currentTime - (30L * 365 * 24 * 60 * 60 * 1000) // 30 years ago
        
        // When validating
        val result = Person.validate("John", "Doe", birthDate, 75.0)
        
        // Then validation should succeed
        assertTrue("Validation should succeed for valid data", result.isValid)
        assertTrue("Should have no errors", result.errors.isEmpty())
    }

    @Test
    fun `when validating empty first name, should return validation error`() {
        // Given empty first name
        val currentTime = System.currentTimeMillis()
        val birthDate = currentTime - (30L * 365 * 24 * 60 * 60 * 1000)
        
        // When validating
        val result = Person.validate("", "Doe", birthDate, 75.0)
        
        // Then validation should fail
        assertFalse("Validation should fail for empty first name", result.isValid)
        assertTrue("Should contain first name error", 
            result.errors.any { it.contains("First name cannot be empty") })
    }

    @Test
    fun `when validating empty last name, should return validation error`() {
        // Given empty last name
        val currentTime = System.currentTimeMillis()
        val birthDate = currentTime - (30L * 365 * 24 * 60 * 60 * 1000)
        
        // When validating
        val result = Person.validate("John", "", birthDate, 75.0)
        
        // Then validation should fail
        assertFalse("Validation should fail for empty last name", result.isValid)
        assertTrue("Should contain last name error", 
            result.errors.any { it.contains("Last name cannot be empty") })
    }

    @Test
    fun `when validating long names, should return validation error`() {
        // Given names longer than 50 characters
        val longName = "a".repeat(51)
        val currentTime = System.currentTimeMillis()
        val birthDate = currentTime - (30L * 365 * 24 * 60 * 60 * 1000)
        
        // When validating
        val result = Person.validate(longName, longName, birthDate, 75.0)
        
        // Then validation should fail
        assertFalse("Validation should fail for long names", result.isValid)
        assertTrue("Should contain first name length error", 
            result.errors.any { it.contains("First name cannot have more than 50 characters") })
        assertTrue("Should contain last name length error", 
            result.errors.any { it.contains("Last name cannot have more than 50 characters") })
    }

    @Test
    fun `when validating invalid weight, should return validation error`() {
        // Given invalid weights
        val currentTime = System.currentTimeMillis()
        val birthDate = currentTime - (30L * 365 * 24 * 60 * 60 * 1000)
        
        // When validating zero weight
        val resultZero = Person.validate("John", "Doe", birthDate, 0.0)
        
        // Then validation should fail
        assertFalse("Validation should fail for zero weight", resultZero.isValid)
        assertTrue("Should contain weight error", 
            resultZero.errors.any { it.contains("Weight must be greater than 0") })
        
        // When validating excessive weight
        val resultHigh = Person.validate("John", "Doe", birthDate, 1001.0)
        
        // Then validation should fail
        assertFalse("Validation should fail for excessive weight", resultHigh.isValid)
        assertTrue("Should contain weight limit error", 
            resultHigh.errors.any { it.contains("Weight must be less than 1000kg") })
    }

    @Test
    fun `when validating future birth date, should return validation error`() {
        // Given future birth date
        val futureDate = System.currentTimeMillis() + (365L * 24 * 60 * 60 * 1000) // 1 year in future
        
        // When validating
        val result = Person.validate("John", "Doe", futureDate, 75.0)
        
        // Then validation should fail
        assertFalse("Validation should fail for future birth date", result.isValid)
        assertTrue("Should contain future date error", 
            result.errors.any { it.contains("Birth date cannot be in the future") })
    }

    @Test
    fun `when validating invalid birth date, should return validation error`() {
        // Given invalid birth date
        val invalidDate = -1L
        
        // When validating
        val result = Person.validate("John", "Doe", invalidDate, 75.0)
        
        // Then validation should fail
        assertFalse("Validation should fail for invalid birth date", result.isValid)
        assertTrue("Should contain invalid date error", 
            result.errors.any { it.contains("Birth date must be valid") })
    }

    @Test
    fun `when validating multiple errors, should return all errors`() {
        // Given multiple invalid inputs
        val futureDate = System.currentTimeMillis() + (365L * 24 * 60 * 60 * 1000)
        
        // When validating
        val result = Person.validate("", "", futureDate, -5.0)
        
        // Then validation should fail with multiple errors
        assertFalse("Validation should fail for multiple errors", result.isValid)
        assertEquals("Should have 4 errors", 4, result.errors.size)
    }
}