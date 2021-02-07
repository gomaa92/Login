package com.sgn.apps.logintask.domain.interactor

import android.util.Patterns
import com.sgn.apps.logintask.presentation.util.ValidationErrorCode

class EmailValidationUseCase {
    suspend fun build(param: String): ValidationErrorCode {
        if (EmptyValidation().build(param)!!) {
            return ValidationErrorCode.EMPTY_EMAIL
        } else if (!Patterns.EMAIL_ADDRESS.matcher(param.trim()).matches()) {
            return ValidationErrorCode.INVALID_EMAIL
        }
        return ValidationErrorCode.VALID_EMAIL
    }
}