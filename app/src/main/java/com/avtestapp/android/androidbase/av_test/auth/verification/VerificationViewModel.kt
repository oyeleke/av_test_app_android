package com.avtestapp.android.androidbase.av_test.auth.verification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.avtestapp.android.androidbase.PrefKeys
import com.avtestapp.android.androidbase.av_test.models.response.LoginSignUpResponse
import com.avtestapp.android.androidbase.av_test.models.response.PasswordResetResponse
import com.avtestapp.android.androidbase.av_test.repository.AuthRepository
import com.avtestapp.android.androidbase.base.BaseViewModel
import com.avtestapp.android.androidbase.networkutils.LoadingStatus
import com.avtestapp.android.androidbase.networkutils.Result
import com.avtestapp.android.androidbase.utils.Form
import com.avtestapp.android.androidbase.utils.FormField
import com.avtestapp.android.androidbase.utils.FormFieldValidators.validateNotEmpty
import com.avtestapp.android.androidbase.utils.PrefsUtils
import com.avtestapp.android.androidbase.utils.ResourceProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

class VerificationViewModel @Inject constructor(
    val authRepository: AuthRepository,
    val prefsUtils: PrefsUtils,
    val resourceProvider: ResourceProvider
) : BaseViewModel() {
    private val _verifyRegistrationCode = MutableLiveData<Boolean>()
    val verifyRegistration: LiveData<Boolean>
        get() = _verifyRegistrationCode

    private val _verifyPasswordResetCode = MutableLiveData<Boolean>()
    val verifyPasswordResetCode: LiveData<Boolean>
        get() = _verifyPasswordResetCode

    val otpCode = Form()

    var code1 =
        FormField<String>().apply {
            addValidator {
                validateNotEmpty()
            }

            addTo(otpCode)
        }

    var code2 =
        FormField<String>().apply {
            addValidator {
                validateNotEmpty()
            }

            addTo(otpCode)
        }

    var code3 =
        FormField<String>().apply {
            addValidator {
                validateNotEmpty()
            }

            addTo(otpCode)
        }

    var code4 =
        FormField<String>().apply {
            addValidator {
                validateNotEmpty()
            }

            addTo(otpCode)
        }

    fun verifyRegistrationCode() {
        if (!otpCode.verify()) return

        _loadingStatus.value = LoadingStatus.Loading("verifying your registration please wait ...")
        val otpCode = "${code1.value}${code2.value}${code3.value}${code4.value}"

        val token = prefsUtils.getPrefAsObject(
            PrefKeys.USER_RESPONSE,
            LoginSignUpResponse::class.java
        ).tokenData.token
        val bearer = "Bearer ".plus(token)
        viewModelScope.launch {
            when (val result = authRepository.verifyUserAfterReg(bearer, otpCode)) {
                is Result.Success -> {
                    _verifyRegistrationCode.value = result.result.verified
                    _loadingStatus.value = LoadingStatus.Success
                }

                is Result.Error -> {
                    _loadingStatus.value =
                        LoadingStatus.Error(result.errorCode, result.errorMessage)
                }
            }
        }
    }


    fun verifyCodeResetPassword() {
        if (!otpCode.verify()) return

        _loadingStatus.value = LoadingStatus.Loading("verifying your registration please wait ...")
        val otpCode = "${code1.value}${code2.value}${code3.value}${code4.value}"

        val email = prefsUtils.getPrefAsObject(
            PrefKeys.GENERIC_AUTH_RESPONSE,
            PasswordResetResponse::class.java
        ).email
        viewModelScope.launch {
            when (val result = authRepository.verifyPasswordResetCode(otpCode, email)) {
                is Result.Success -> {
                    _verifyPasswordResetCode.value = true
                    _loadingStatus.value = LoadingStatus.Success
                }

                is Result.Error -> {
                    _loadingStatus.value =
                        LoadingStatus.Error(result.errorCode, result.errorMessage)
                }
            }
        }
    }


    override fun addAllLiveDataToObservablesList() {
        observablesList.add(verifyPasswordResetCode)
        observablesList.add(verifyRegistration)
    }
    // TODO: Implement the ViewModel
}