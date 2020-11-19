package com.avtestapp.android.androidbase.av_test.auth.reset_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avtestapp.android.androidbase.PrefKeys
import com.avtestapp.android.androidbase.av_test.models.response.PasswordResetResponse
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

class ResetPasswordViewModel @Inject constructor(
    val authRepository: AuthRepository,
    val prefsUtils: PrefsUtils,
    val resourceProvider: ResourceProvider
) : BaseViewModel() {

    private val _observePasswordSuccess = MutableLiveData<Boolean>()
    val observePasswordSuccess: LiveData<Boolean>
        get() = _observePasswordSuccess

    val passwordForm = Form()

    var password1 = FormField<String>().apply {
        addValidator { validateNotEmpty() }
        addTo(passwordForm)
    }

    var password2 = FormField<String>().apply {
        addValidators({ validateNotEmpty() }, { validatePasswordsMatch(password1.value!!) })
        addTo(passwordForm)
    }

    fun resetPassword() {
        if (!passwordForm.verify()) return

        _loadingStatus.value = LoadingStatus.Loading("Reseting your password please wait ...")
        val id = prefsUtils.getPrefAsObject(
            PrefKeys.GENERIC_AUTH_RESPONSE,
            PasswordResetResponse::class.java
        )._id

        viewModelScope.launch {
            when (val result =
                authRepository.createNewPassword(id = id, newPassword = password1.value!!)) {
                is Result.Success -> {
                    _observePasswordSuccess.value = true
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
        observablesList.add(observePasswordSuccess)
    }
    // TODO: Implement the ViewModel
}