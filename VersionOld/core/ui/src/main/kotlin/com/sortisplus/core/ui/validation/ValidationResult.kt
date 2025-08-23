package com.sortisplus.core.ui.validation

/**
 * Represents the result of a validation operation
 */
sealed class ValidationResult {
    data object Valid : ValidationResult()
    data class Invalid(val errorMessage: String) : ValidationResult()

    val isValid: Boolean
        get() = this is Valid
}

/**
 * Interface for field validators
 * @param T the type of value to validate
 */
interface Validator<T> {
    fun validate(value: T): ValidationResult
}

/**
 * Combines multiple validators with AND logic
 */
class CompositeValidator<T>(
    private val validators: List<Validator<T>>
) : Validator<T> {
    override fun validate(value: T): ValidationResult {
        validators.forEach { validator ->
            val result = validator.validate(value)
            if (!result.isValid) {
                return result
            }
        }
        return ValidationResult.Valid
    }
}

/**
 * Extension function to combine validators
 */
fun <T> List<Validator<T>>.asComposite(): Validator<T> = CompositeValidator(this)

/**
 * Extension function to add validators
 */
infix fun <T> Validator<T>.and(other: Validator<T>): Validator<T> =
    CompositeValidator(listOf(this, other))
