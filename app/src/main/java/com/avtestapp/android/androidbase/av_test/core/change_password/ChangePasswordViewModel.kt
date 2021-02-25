package com.avtestapp.android.androidbase.av_test.core.change_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.avtestapp.android.androidbase.PrefKeys
import com.avtestapp.android.androidbase.av_test.models.requests.ChangePasswordRequest
import com.avtestapp.android.androidbase.av_test.models.response.LoginSignUpResponse
import com.avtestapp.android.androidbase.av_test.repository.AuthRepository
import com.avtestapp.android.androidbase.base.BaseViewModel
import com.avtestapp.android.androidbase.networkutils.LoadingStatus
import com.avtestapp.android.androidbase.networkutils.Result
import com.avtestapp.android.androidbase.utils.Form
import com.avtestapp.android.androidbase.utils.FormField
import com.avtestapp.android.androidbase.utils.FormFieldValidators.validateNotEmpty
import com.avtestapp.android.androidbase.utils.FormFieldValidators.validatePasswordsMatch
import com.avtestapp.android.androidbase.utils.PrefsUtils
import com.avtestapp.android.androidbase.utils.ResourceProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChangePasswordViewModel @Inject constructor(
    val resourceProvider: ResourceProvider,
    val prefsUtils: PrefsUtils,
    val authRepository: AuthRepository
) : BaseViewModel() {

    private val _passwordChangeSuccessful = MutableLiveData<Boolean>()

    val passwordChangeSuccessful: LiveData<Boolean>
        get() = _passwordChangeSuccessful

    val changePasswordForm = Form()

    val oldPassword =
        FormField<String>().apply {
            addValidator { validateNotEmpty() }
            addTo(changePasswordForm)
        }

    val newPassword =
        FormField<String>().apply {
            addValidator { validateNotEmpty() }
            addTo(changePasswordForm)
        }

    val confirmNewPassword =
        FormField<String>().apply {
            addValidator { validateNotEmpty() }
            addValidator { validatePasswordsMatch(newPassword.value ?: "") }
            addTo(changePasswordForm)
        }

    fun changePassword() {

        if (!changePasswordForm.verify()) return

        val token = prefsUtils.getPrefAsObject(
            PrefKeys.USER_PROFILE,
            LoginSignUpResponse::class.java
        ).tokenData.token
        val bearer = "Bearer ".plus(token)

        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.Loading("Please wait ...")
            when (val response = authRepository.changePassword(
                bearerAndToken = bearer,
                changePasswordRequest = ChangePasswordRequest(
                    oldPassword = oldPassword.value!!,
                    newPassword = newPassword.value!!
                )
            )) {
                is Result.Success -> {
                    _loadingStatus.value = LoadingStatus.Success
                    _passwordChangeSuccessful.value = true
                }

                is Result.Error -> {
                    _loadingStatus.value =
                        LoadingStatus.Error(response.errorCode, response.errorMessage)
                    _passwordChangeSuccessful.value = false
                }
            }
        }
    }

    override fun addAllLiveDataToObservablesList() {

    }

// TODO: Implement the ViewModel
}