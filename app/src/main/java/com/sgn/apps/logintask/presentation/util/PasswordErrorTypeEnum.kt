package com.sgn.apps.logintask.presentation.util

enum class PasswordErrorTypeEnum(val errorType: String) {
    INVALID_START_WITH_DIGIT("start_with_digit"),
    INVALID_ALLOW_SPECIAL_CHARACTERS("allow_special_characters"),
    INVALID_LIMIT_PASSWORD("limit_password"),
    INVALID_AT_LEAST_ONE_DIGIT("al_least_one_digit");
}