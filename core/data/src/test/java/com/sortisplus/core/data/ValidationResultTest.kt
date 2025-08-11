package com.sortisplus.core.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for ValidationResult utility class
 * 
 * Tests the validation result wrapper functionality including
 * success and failure cases with proper error handling.
 */
class ValidationResultTest {

    @Test
    fun `when creating success result, should have valid state with no errors`() {
        // When creating success result
        val result = ValidationResult.success()
        
        // Then should be valid with no errors
        assertTrue("Result should be valid", result.isValid)
        assertTrue("Should have no errors", result.errors.isEmpty())
    }

    @Test
    fun `when creating failure result with single error, should have invalid state with error`() {
        // Given single error message
        val errorMessage = "Invalid input"
        
        // When creating failure result
        val result = ValidationResult.failure(errorMessage)
        
        // Then should be invalid with error
        assertFalse("Result should be invalid", result.isValid)
        assertEquals("Should have one error", 1, result.errors.size)
        assertEquals("Should contain error message", errorMessage, result.errors.first())
    }

    @Test
    fun `when creating failure result with multiple errors, should have invalid state with all errors`() {
        // Given multiple error messages
        val error1 = "First error"
        val error2 = "Second error"
        val error3 = "Third error"
        
        // When creating failure result
        val result = ValidationResult.failure(error1, error2, error3)
        
        // Then should be invalid with all errors
        assertFalse("Result should be invalid", result.isValid)
        assertEquals("Should have three errors", 3, result.errors.size)
        assertTrue("Should contain first error", result.errors.contains(error1))
        assertTrue("Should contain second error", result.errors.contains(error2))
        assertTrue("Should contain third error", result.errors.contains(error3))
    }

    @Test
    fun `when creating failure result with no errors, should have invalid state with empty errors`() {
        // When creating failure result with no error messages
        val result = ValidationResult.failure()
        
        // Then should be invalid with empty errors
        assertFalse("Result should be invalid", result.isValid)
        assertTrue("Should have no errors", result.errors.isEmpty())
    }

    @Test
    fun `when creating validation result directly, should preserve state and errors`() {
        // Given validation result with specific state and errors
        val errors = listOf("Error 1", "Error 2")
        val result = ValidationResult(isValid = false, errors = errors)
        
        // Then should preserve the state and errors
        assertFalse("Result should be invalid", result.isValid)
        assertEquals("Should have correct error count", 2, result.errors.size)
        assertEquals("Should preserve error list", errors, result.errors)
    }

    @Test
    fun `when creating valid result with errors, should preserve inconsistent state`() {
        // Given validation result with valid=true but with errors (edge case)
        val errors = listOf("Some error")
        val result = ValidationResult(isValid = true, errors = errors)
        
        // Then should preserve the inconsistent state as created
        assertTrue("Result should be valid as specified", result.isValid)
        assertEquals("Should have one error", 1, result.errors.size)
        assertEquals("Should preserve error", "Some error", result.errors.first())
    }
}