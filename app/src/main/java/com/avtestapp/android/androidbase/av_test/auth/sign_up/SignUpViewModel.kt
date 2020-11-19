package com.avtestapp.android.androidbase.av_test.auth.sign_up

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avtestapp.android.androidbase.Interactors.SignUpUser
import com.avtestapp.android.androidbase.PrefKeys
import com.avtestapp.android.androidbase.av_test.models.requests.RegisterRequest
import com.avtestapp.android.androidbase.av_test.models.response.LoginSignUpResponse
import com.avtestapp.android.androidbase.av_test.models.response.ProfileResponse
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

class SignUpViewModel @Inject constructor(
    val resourceProvider: ResourceProvider,
    val signUpUser: SignUpUser,
    val prefsUtils: PrefsUtils
) : BaseViewModel() {

    val signUpForm = Form()
    private val _userProfile = MutableLiveData<LoginSignUpResponse>()
    val userProfile: LiveData<LoginSignUpResponse>
        get() = _userProfile


    val email =
        FormField<String>().apply {
            addValidators(
                { validateNotEmpty() },
                { validateEmailAddress() }
            )
            addTo(signUpForm)
        }

    val firstName =
        FormField<String>().apply {
            addValidator { validateNotEmpty() }
            addTo(signUpForm)
        }

    val lastName =
        FormField<String>().apply {
            addValidator { validateNotEmpty() }
            addTo(signUpForm)
        }

    val password =
        FormField<String>().apply {
            addValidator { validateNotEmpty() }
            addTo(signUpForm)
        }

    fun signUpUser() {
        if (!signUpForm.verify()) return
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.Loading("please wait ...")
            when(val r = signUpUser.signUpUser(
                RegisterRequest(
                    email = email.value!!,
                    firstName = firstName.value!!,
                    lastName = lastName.value!!,
                    password = password.value!!
                )
            )){
                is Result.Success -> {
                    _loadingStatus.value = LoadingStatus.Success
                    _userProfile.value = r.result
                    prefsUtils.putObject(PrefKeys.USER_RESPONSE, r.result)
r                }

                is Result.Error -> {
                    _loadingStatus.value = LoadingStatus.Error(r.errorCode, r.errorMessage)
                }
            }
        }
    }


    override fun addAllLiveDataToObservablesList() {
        observablesList.add(userProfile)
    }
}