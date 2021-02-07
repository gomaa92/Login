package com.sgn.apps.logintask.domain.interactor

import com.sgn.apps.logintask.presentation.util.PasswordErrorTypeEnum
import com.sgn.apps.logintask.presentation.util.ValidationErrorCode

class PasswordValidationUseCase {
    private val rejexAtLeastOneDigit = Regex(".*[0-9].*")
    private val rejexStartWithDigit = Regex("^[A-Za-z].*$")
    private val rejexLimitPassword = Regex("^.{7,15}$")
    private val rejexAllowSpecialCharacter = Regex("^[A-Za-z0-9!@#$%]*$")
    private var mPasswordErrorTypeEnum: String? = null
    suspend fun build(param: String): ValidationErrorCode {
        if (EmptyValidation().build(param)!!) {
            return ValidationErrorCode.EMPTY_PASSWORD
        } else if (!validatePassword(param)) {
            return if (mPasswordErrorTypeEnum == PasswordErrorTypeEnum.INVALID_START_WITH_DIGIT.errorType) {
                ValidationErrorCode.INVALID_START_WITH_DIGIT
            } else if (mPasswordErrorTypeEnum == PasswordErrorTypeEnum.INVALID_ALLOW_SPECIAL_CHARACTERS.errorType) {
                ValidationErrorCode.INVALID_ALLOW_SPECIAL_CHARACTERS
            } else if (mPasswordErrorTypeEnum == PasswordErrorTypeEnum.INVALID_LIMIT_PASSWORD.errorType) {
                ValidationErrorCode.INVALID_LIMIT_PASSWORD
            } else if (mPasswordErrorTypeEnum == PasswordErrorTypeEnum.INVALID_AT_LEAST_ONE_DIGIT.errorType) {
                ValidationErrorCode.INVALID_AT_LEAST_ONE_DIGIT
            } else {
                ValidationErrorCode.INVALID_PASSWORD
            }
        }
        return ValidationErrorCode.VALID_PASSWORD
    }

    suspend fun build(password: String, validatePasswordFormat: Boolean): ValidationErrorCode? {
        if (EmptyValidation().build(password)!!) {
            return ValidationErrorCode.EMPTY_PASSWORD
        } else if (validatePasswordFormat && !validatePassword(password)) {
            return if (mPasswordErrorTypeEnum == PasswordErrorTypeEnum.INVALID_START_WITH_DIGIT.errorType) {
                ValidationErrorCode.INVALID_START_WITH_DIGIT
            } else if (mPasswordErrorTypeEnum == PasswordErrorTypeEnum.INVALID_ALLOW_SPECIAL_CHARACTERS.errorType) {
                ValidationErrorCode.INVALID_ALLOW_SPECIAL_CHARACTERS
            } else if (mPasswordErrorTypeEnum == PasswordErrorTypeEnum.INVALID_LIMIT_PASSWORD.errorType) {
                ValidationErrorCode.INVALID_LIMIT_PASSWORD
            } else if (mPasswordErrorTypeEnum == PasswordErrorTypeEnum.INVALID_AT_LEAST_ONE_DIGIT.errorType) {
                ValidationErrorCode.INVALID_AT_LEAST_ONE_DIGIT
            } else {
                ValidationErrorCode.INVALID_PASSWORD
            }
        }
        return ValidationErrorCode.VALID_PASSWORD
    }

    private fun validatePassword(password: String): Boolean {
        if (!password.matches(rejexStartWithDigit)) {
            mPasswordErrorTypeEnum = PasswordErrorTypeEnum.INVALID_START_WITH_DIGIT.errorType
            return false
        } else if (!password.matches(rejexAllowSpecialCharacter)) {
            mPasswordErrorTypeEnum =
                PasswordErrorTypeEnum.INVALID_ALLOW_SPECIAL_CHARACTERS.errorType
            return false
        } else if (!password.matches(rejexLimitPassword)) {
            mPasswordErrorTypeEnum = PasswordErrorTypeEnum.INVALID_LIMIT_PASSWORD.errorType
            return false
        } else if (!password.matches(rejexAtLeastOneDigit)) {
            mPasswordErrorTypeEnum = PasswordErrorTypeEnum.INVALID_AT_LEAST_ONE_DIGIT.errorType
            return false
        }
        return true
    }
}