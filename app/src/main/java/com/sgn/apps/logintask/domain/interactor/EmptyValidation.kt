package com.sgn.apps.logintask.domain.interactor

class EmptyValidation {
    suspend fun build(param: String?): Boolean? {
        return param == null || param.trim().isEmpty()
    }
}