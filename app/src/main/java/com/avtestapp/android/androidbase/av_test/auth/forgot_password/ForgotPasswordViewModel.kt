package com.avtestapp.android.androidbase.av_test.auth.forgot_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avtestapp.android.androidbase.PrefKeys.Companion.GENERIC_AUTH_RESPONSE
import com.avtestapp.android.androidbase.av_test.models.response.PasswordResetResponse
import com.avtestapp.android.androidbase.av_test.repository.AuthRepository
import com.avtestapp.android.androidbase.base.BaseViewModel
import com.avtestapp.android.androidbase.networkutils.LoadingStatus
import com.avtestapp.android.androidbase.networkutils.Result
import com.avtestapp.android.androidbase.utils.Form
import com.avtestapp.android.androidbase.utils.FormField
import com.avtestapp.android.androidbase.utils.FormFieldValidators.validateEmailAddress
import com.avtestapp.android.androidbase.utils.FormFieldValidators.validateNotEmpty
import com.avtestapp.android.androidbase.utils.PrefsUtils
import com.avtestapp.android.androidbase.utils.ResourceProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

class ForgotPasswordViewModel @Inject constructor(
    val authRepository: AuthRepository,
    val resourceProvider: ResourceProvider,
    val prefsUtils: PrefsUtils
) : BaseViewModel() {

    val _passwordResetResponseObserver = MutableLiveData<PasswordResetResponse>()
    val passwordResetResponseObserver: LiveData<PasswordResetResponse> get() = _passwordResetResponseObserver
    val emailForm = Form()

    val email = FormField<String>().apply {
        addValidators(
            { validateNotEmpty() },
            { validateEmailAddress() }
        )
        addTo(emailForm)
    }

    fun initializePassword() {
        if (!emailForm.verify()) return
        _loadingStatus.value = LoadingStatus.Loading("initializing password reset ....")
        viewModelScope.launch {
            when (val result = authRepository.initiatePasswordReset(email = email.value!!)) {
                is Result.Success -> {
                    _passwordResetResponseObserver.value = result.result
                    prefsUtils.putObject(GENERIC_AUTH_RESPONSE, result.result)
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
        observablesList.add(passwordResetResponseObserver)
    }
    // TODO: Implement the ViewModel
}