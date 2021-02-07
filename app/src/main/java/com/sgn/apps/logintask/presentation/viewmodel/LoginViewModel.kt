package com.sgn.apps.logintask.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.sgn.apps.logintask.domain.interactor.EmailValidationUseCase
import com.sgn.apps.logintask.domain.interactor.PasswordValidationUseCase
import com.sgn.apps.logintask.presentation.util.ValidationErrorCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val phoneValidationLiveData = MutableLiveData<ValidationErrorCode>()
    private val passwordValidationLiveData = MutableLiveData<ValidationErrorCode>()
    private var phoneValidationUseCase: EmailValidationUseCase = EmailValidationUseCase()
    private var passwordValidationUseCase: PasswordValidationUseCase = PasswordValidationUseCase()

    lateinit var googleSignInClient: GoogleSignInClient
    private val _socialLoginObservable = MutableLiveData<Boolean>()
    val socialLoginObservable: LiveData<Boolean> = _socialLoginObservable

    fun emailValidation(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            phoneValidationLiveData.postValue(phoneValidationUseCase.build(email))
        }


    }

    fun getEmailValidationLiveData(): MutableLiveData<ValidationErrorCode> {
        return phoneValidationLiveData
    }

    fun passwordValidation(password: String) {
        viewModelScope.launch(Dispatchers.Default) {
            passwordValidationLiveData.postValue(passwordValidationUseCase.build(password))
        }


    }

    fun getPasswordValidationLiveData(): MutableLiveData<ValidationErrorCode> {
        return passwordValidationLiveData
    }

    fun handleGoogleSignInResult(signedInAccountFromIntent: Task<GoogleSignInAccount>?) {
        val account = signedInAccountFromIntent?.getResult(ApiException::class.java)
        Log.i("GOOGLEDATA",account?.displayName+"")
        _socialLoginObservable.postValue(true)
        googleSignInClient.signOut()

    }

}