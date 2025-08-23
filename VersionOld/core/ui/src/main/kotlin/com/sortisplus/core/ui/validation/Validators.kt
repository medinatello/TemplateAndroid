package com.sortisplus.core.ui.validation

import android.util.Patterns

/**
 * Common string validators following business rules
 */
object StringValidators {

    /**
     * Validates that a string is not empty
     */
    class RequiredValidator(
        private val errorMessage: String = "This field is required"
    ) : Validator<String> {
        override fun validate(value: String): ValidationResult {
            return if (value.isBlank()) {
                ValidationResult.Invalid(errorMessage)
            } else {
                ValidationResult.Valid
            }
        }
    }

    /**
     * Validates minimum length of a string
     */
    class MinLengthValidator(
        private val minLength: Int,
        private val errorMessage: String = "Must be at least $minLength characters"
    ) : Validator<String> {
        override fun validate(value: String): ValidationResult {
            return if (value.length < minLength) {
                ValidationResult.Invalid(errorMessage.replace("$minLength", minLength.toString()))
            } else {
                ValidationResult.Valid
            }
        }
    }

    /**
     * Validates maximum length of a string
     */
    class MaxLengthValidator(
        private val maxLength: Int,
        private val errorMessage: String = "Must be no more than $maxLength characters"
    ) : Validator<String> {
        override fun validate(value: String): ValidationResult {
            return if (value.length > maxLength) {
                ValidationResult.Invalid(errorMessage.replace("$maxLength", maxLength.toString()))
            } else {
                ValidationResult.Valid
            }
        }
    }

    /**
     * Validates email format using Android's built-in pattern
     */
    class EmailValidator(
        private val errorMessage: String = "Please enter a valid email address"
    ) : Validator<String> {
        override fun validate(value: String): ValidationResult {
            return if (value.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
                ValidationResult.Invalid(errorMessage)
            } else {
                ValidationResult.Valid
            }
        }
    }

    /**
     * Validates password strength
     */
    class PasswordValidator(
        private val minLength: Int = 6,
        private val requireUppercase: Boolean = false,
        private val requireLowercase: Boolean = false,
        private val requireDigit: Boolean = false,
        private val requireSpecialChar: Boolean = false
    ) : Validator<String> {

        override fun validate(value: String): ValidationResult {
            if (value.length < minLength) {
                return ValidationResult.Invalid("Password must be at least $minLength characters")
            }

            if (requireUppercase && !value.any { it.isUpperCase() }) {
                return ValidationResult.Invalid("Password must contain at least one uppercase letter")
            }

            if (requireLowercase && !value.any { it.isLowerCase() }) {
                return ValidationResult.Invalid("Password must contain at least one lowercase letter")
            }

            if (requireDigit && !value.any { it.isDigit() }) {
                return ValidationResult.Invalid("Password must contain at least one digit")
            }

            if (requireSpecialChar && !value.any { !it.isLetterOrDigit() }) {
                return ValidationResult.Invalid("Password must contain at least one special character")
            }

            return ValidationResult.Valid
        }
    }

    /**
     * Validates that two strings match (useful for password confirmation)
     */
    class MatchValidator(
        private val targetValue: String,
        private val errorMessage: String = "Values do not match"
    ) : Validator<String> {
        override fun validate(value: String): ValidationResult {
            return if (value != targetValue) {
                ValidationResult.Invalid(errorMessage)
            } else {
                ValidationResult.Valid
            }
        }
    }

    /**
     * Validates string against a regex pattern
     */
    class PatternValidator(
        private val pattern: Regex,
        private val errorMessage: String = "Invalid format"
    ) : Validator<String> {
        override fun validate(value: String): ValidationResult {
            return if (!pattern.matches(value)) {
                ValidationResult.Invalid(errorMessage)
            } else {
                ValidationResult.Valid
            }
        }
    }
}

/**
 * Convenience factory functions for common validators
 */
object Validators {
    fun required(message: String = "This field is required") =
        StringValidators.RequiredValidator(message)

    fun minLength(length: Int, message: String = "Must be at least $length characters") =
        StringValidators.MinLengthValidator(length, message)

    fun maxLength(length: Int, message: String = "Must be no more than $length characters") =
        StringValidators.MaxLengthValidator(length, message)

    fun email(message: String = "Please enter a valid email address") =
        StringValidators.EmailValidator(message)

    fun password(
        minLength: Int = 6,
        requireUppercase: Boolean = false,
        requireLowercase: Boolean = false,
        requireDigit: Boolean = false,
        requireSpecialChar: Boolean = false
    ) = StringValidators.PasswordValidator(
        minLength = minLength,
        requireUppercase = requireUppercase,
        requireLowercase = requireLowercase,
        requireDigit = requireDigit,
        requireSpecialChar = requireSpecialChar
    )

    fun match(targetValue: String, message: String = "Values do not match") =
        StringValidators.MatchValidator(targetValue, message)

    fun pattern(pattern: Regex, message: String = "Invalid format") =
        StringValidators.PatternValidator(pattern, message)
}
