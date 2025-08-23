package com.sortisplus.core.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for DatabaseResult sealed class
 * 
 * Tests the Result pattern implementation for safe error handling
 * in database operations and other potentially failing operations.
 */
class DatabaseResultTest {

    @Test
    fun `when creating success result with data, should contain correct data`() {
        // Given some data
        val testData = "Success data"
        
        // When creating success result
        val result = DatabaseResult.Success(testData)
        
        // Then should contain the data
        assertTrue("Result should be Success type", result is DatabaseResult.Success)
        assertEquals("Should contain correct data", testData, result.data)
    }

    @Test
    fun `when creating success result with null data, should handle null correctly`() {
        // Given null data
        val testData: String? = null
        
        // When creating success result
        val result = DatabaseResult.Success(testData)
        
        // Then should handle null correctly
        assertTrue("Result should be Success type", result is DatabaseResult.Success)
        assertEquals("Should contain null data", testData, result.data)
    }

    @Test
    fun `when creating success result with complex data, should preserve object`() {
        // Given complex data object
        val person = Person(
            id = 1L,
            firstName = "John",
            lastName = "Doe",
            birthDateMillis = System.currentTimeMillis() - (25L * 365 * 24 * 60 * 60 * 1000),
            weightKg = 75.0,
            isLeftHanded = false
        )
        
        // When creating success result
        val result = DatabaseResult.Success(person)
        
        // Then should preserve the object
        assertTrue("Result should be Success type", result is DatabaseResult.Success)
        assertEquals("Should contain the person object", person, result.data)
        assertEquals("Should preserve person ID", 1L, result.data.id)
        assertEquals("Should preserve first name", "John", result.data.firstName)
    }

    @Test
    fun `when creating error result with exception, should contain exception`() {
        // Given an exception
        val exception = RuntimeException("Database error")
        
        // When creating error result
        val result = DatabaseResult.Error(exception)
        
        // Then should contain the exception
        assertTrue("Result should be Error type", result is DatabaseResult.Error)
        assertEquals("Should contain correct exception", exception, result.exception)
        assertEquals("Should preserve exception message", "Database error", result.exception.message)
    }

    @Test
    fun `when creating error result with different exception types, should handle correctly`() {
        // Given different exception types
        val illegalArgException = IllegalArgumentException("Invalid argument")
        val illegalStateException = IllegalStateException("Invalid state")
        
        // When creating error results
        val result1 = DatabaseResult.Error(illegalArgException)
        val result2 = DatabaseResult.Error(illegalStateException)
        
        // Then should handle different types correctly
        assertTrue("First result should be Error type", result1 is DatabaseResult.Error)
        assertTrue("Second result should be Error type", result2 is DatabaseResult.Error)
        assertTrue("Should preserve IllegalArgumentException", 
            result1.exception is IllegalArgumentException)
        assertTrue("Should preserve IllegalStateException", 
            result2.exception is IllegalStateException)
        assertEquals("Should preserve first exception message", 
            "Invalid argument", result1.exception.message)
        assertEquals("Should preserve second exception message", 
            "Invalid state", result2.exception.message)
    }

    @Test
    fun `when pattern matching on success result, should access data`() {
        // Given success result
        val testData = 42
        val result: DatabaseResult<Int> = DatabaseResult.Success(testData)
        
        // When pattern matching
        val extractedValue = when (result) {
            is DatabaseResult.Success -> result.data
            is DatabaseResult.Error -> -1
        }
        
        // Then should extract correct data
        assertEquals("Should extract correct data", testData, extractedValue)
    }

    @Test
    fun `when pattern matching on error result, should access exception`() {
        // Given error result
        val exception = RuntimeException("Test error")
        val result: DatabaseResult<String> = DatabaseResult.Error(exception)
        
        // When pattern matching
        val extractedMessage = when (result) {
            is DatabaseResult.Success -> "No error"
            is DatabaseResult.Error -> result.exception.message
        }
        
        // Then should extract exception message
        assertEquals("Should extract exception message", "Test error", extractedMessage)
    }

    @Test
    fun `when chaining operations on success, should continue with success`() {
        // Given successful result
        val initialData = 10
        val result: DatabaseResult<Int> = DatabaseResult.Success(initialData)
        
        // When transforming through chain
        val transformedResult = when (result) {
            is DatabaseResult.Success -> {
                val doubled = result.data * 2
                DatabaseResult.Success(doubled)
            }
            is DatabaseResult.Error -> result
        }
        
        // Then should continue with transformed success
        assertTrue("Result should remain Success", transformedResult is DatabaseResult.Success)
        if (transformedResult is DatabaseResult.Success) {
            assertEquals("Should have doubled value", 20, transformedResult.data)
        }
    }

    @Test
    fun `when chaining operations on error, should propagate error`() {
        // Given error result
        val exception = RuntimeException("Initial error")
        val result: DatabaseResult<Int> = DatabaseResult.Error(exception)
        
        // When attempting transformation
        val transformedResult = when (result) {
            is DatabaseResult.Success -> DatabaseResult.Success(result.data * 2)
            is DatabaseResult.Error -> result // Propagate error
        }
        
        // Then should propagate the original error
        assertTrue("Result should remain Error", transformedResult is DatabaseResult.Error)
        if (transformedResult is DatabaseResult.Error) {
            assertEquals("Should preserve original exception", exception, transformedResult.exception)
        }
    }
}