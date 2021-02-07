package com.sgn.apps.logintask.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.sgn.apps.logintask.R
import com.sgn.apps.logintask.presentation.util.ValidationErrorCode
import com.sgn.apps.logintask.presentation.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_main.*


class LoginActivity : AppCompatActivity() {
    companion object {
        const val GOOGLE_SIGN_IN_REQUEST_CODE = 123
    }

    lateinit var viewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        bindEmailChange()
        bindPasswordChange()
        bindEmailValidation()
        bindPasswordValidation()
        login()
        bindSocialMediaActions()
        bindSocialLoginResult()
    }

    private fun bindSocialLoginResult() {
        viewModel.socialLoginObservable.observe(this) {
            Toast.makeText(
                this,
                getString(R.string.login_has_been_done_sucessfully),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun login() {
        loginButton.setOnClickListener {
            if (isInputsValid()) {
                Toast.makeText(
                    this,
                    getString(R.string.login_has_been_done_sucessfully),
                    Toast.LENGTH_LONG
                ).show()

            } else {
                viewModel.passwordValidation(login_password_text_input_edit_text.text.toString())
                viewModel.emailValidation(login_email_text_input_edit_text.text.toString())

            }
        }
    }

    private fun bindEmailChange() {
        login_email_text_input_edit_text.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.emailValidation(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Fires right before text is changing

            }

            override fun afterTextChanged(s: Editable) {
                viewModel.emailValidation(s.toString())


            }
        })
    }

    private fun bindPasswordChange() {
        login_password_text_input_edit_text.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.passwordValidation(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Fires right before text is changing
                // viewModel.passwordValidation(s.toString())

            }

            override fun afterTextChanged(s: Editable) {
                //  viewModel.passwordValidation(s.toString())


            }
        })
    }

    private fun bindEmailValidation() {
        viewModel.getEmailValidationLiveData()
            .observe(this@LoginActivity, androidx.lifecycle.Observer {
                if (it != null) {
                    propagateValidationResultToUI(it)
                }
            })

    }

    private fun bindPasswordValidation() {
        viewModel.getPasswordValidationLiveData()
            .observe(this@LoginActivity, androidx.lifecycle.Observer {
                if (it != null) {
                    propagateValidationResultToUI(it)
                }
            })
    }

    private fun propagateValidationResultToUI(errorCode: ValidationErrorCode?) {
        when (errorCode) {
            ValidationErrorCode.EMPTY_EMAIL -> {
                login_email_text_input_edit_text.requestFocus()
                login_email_text_input_layout.error = getString(R.string.empty_email)
            }

            ValidationErrorCode.INVALID_EMAIL -> {
                login_email_text_input_edit_text.requestFocus()
                login_email_text_input_layout.error = getString(R.string.invalid_email)
            }
            ValidationErrorCode.VALID_EMAIL -> {
                login_email_text_input_layout.error = null
            }
            ValidationErrorCode.EMPTY_PASSWORD -> {
                login_password_text_input_edit_text.requestFocus()
                login_password_text_input_layout.error = getString(R.string.empty_password)
            }
            ValidationErrorCode.INVALID_PASSWORD -> {
                login_password_text_input_edit_text.requestFocus()
                login_password_text_input_layout.error = getString(R.string.invalid_password)
            }
            ValidationErrorCode.INVALID_START_WITH_DIGIT -> {
                login_password_text_input_edit_text.requestFocus()
                login_password_text_input_layout.error = getString(R.string.start_with_digit)
            }
            ValidationErrorCode.INVALID_ALLOW_SPECIAL_CHARACTERS -> {
                login_password_text_input_edit_text.requestFocus()
                login_password_text_input_layout.error =
                    getString(R.string.allow_special_characters)
            }
            ValidationErrorCode.INVALID_LIMIT_PASSWORD -> {
                login_password_text_input_edit_text.requestFocus()
                login_password_text_input_layout.error = getString(R.string.limit_password)
            }
            ValidationErrorCode.INVALID_AT_LEAST_ONE_DIGIT -> {
                login_password_text_input_edit_text.requestFocus()
                login_password_text_input_layout.error = getString(R.string.at_least_one_digit)
            }
            ValidationErrorCode.VALID_PASSWORD -> {
                login_password_text_input_layout.error = null

            }


            else -> {

            }
        }

    }

    private fun isInputsValid(): Boolean {
        return viewModel.getEmailValidationLiveData().value != null &&
                viewModel.getEmailValidationLiveData().value == ValidationErrorCode.VALID_EMAIL &&
                viewModel.getPasswordValidationLiveData().value != null &&
                viewModel.getPasswordValidationLiveData().value == ValidationErrorCode.VALID_PASSWORD


    }

    private fun bindSocialMediaActions() {
        loginWithGmailButton.setOnClickListener {
            setupGoogleSdk()
        }
    }

    private fun setupGoogleSdk() {
        viewModel.googleSignInClient = GoogleSignIn.getClient(
            this, GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
            )
                .requestEmail()
                .requestId()
                .requestProfile()
                .build()
        )
        startActivityForResult(
            viewModel.googleSignInClient.signInIntent,
            GOOGLE_SIGN_IN_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            viewModel.handleGoogleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data))
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}