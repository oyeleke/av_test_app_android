package com.avtestapp.android.androidbase.av_test.onboarding.upload_image

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

class UploadImageViewModel @Inject constructor(
    val authRepository: AuthRepository,
    val prefsUtils: PrefsUtils,
    val resourceProvider: ResourceProvider
) : BaseViewModel() {
    private val _uploadSuccessfulObserver = MutableLiveData<PasswordResetResponse>()
    val uploadSuccessfulObserver: LiveData<PasswordResetResponse> get() = _uploadSuccessfulObserver
    val uploadImageForm = Form()

    var formPath = FormField<String>().apply {
        addValidator { validateNotEmpty() }
        addTo(uploadImageForm)
    }

    fun uploadCustomerImage(){
        if (!uploadImageForm.verify()) return

        val token = prefsUtils.getPrefAsObject(
            PrefKeys.USER_PROFILE,
            LoginSignUpResponse::class.java
        ).tokenData.token
        val bearer = "Bearer ".plus(token)
        _loadingStatus.value = LoadingStatus.Loading("uploading image ...")
        viewModelScope.launch {
            when(val result = authRepository.uploadUserImage(bearer, formPath.value!!)){
                is Result.Success -> {
                    _uploadSuccessfulObserver.value = result.result
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
        observablesList.add(uploadSuccessfulObserver)
    }
    // TODO: Implement the ViewModel
}