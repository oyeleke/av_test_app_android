package com.avtestapp.android.androidbase.av_test.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avtestapp.android.androidbase.Interactors.LoginUser
import com.avtestapp.android.androidbase.PrefKeys
import com.avtestapp.android.androidbase.R
import com.avtestapp.android.androidbase.av_test.models.requests.LoginRequest
import com.avtestapp.android.androidbase.av_test.models.response.LoginSignUpResponse
import com.avtestapp.android.androidbase.av_test.models.response.ProfessionItemsResponse
import com.avtestapp.android.androidbase.av_test.models.response.ProfileResponse
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
import timber.log.Timber
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    val loginUser: LoginUser,
    val authRepository: AuthRepository,
    val prefsUtils: PrefsUtils,
    val resourceProvider: ResourceProvider
) : BaseViewModel() {

    private val _listOfProfessionals = MutableLiveData<List<ProfessionItemsResponse>>()
    val listOfProfessionals: LiveData<List<ProfessionItemsResponse>>
        get() = _listOfProfessionals

    val loginForm = Form()
    private val _userProfile = MutableLiveData<ProfileResponse>()
    val userProfile: LiveData<ProfileResponse>
        get() = _userProfile

    val email =
        FormField<String>().apply {
            addValidator { validateEmailAddress() }
            addTo(loginForm)
        }

    val password =
        FormField<String>().apply {
            addValidator { validateNotEmpty() }
            addTo(loginForm)
        }

    fun logUserIn() {
        if (!loginForm.verify()) return

        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.Loading("please wait ...")
            when(val result = loginUser.loginUser(
                LoginRequest(
                    email = email.value!!,
                    password = password.value!!
                )
            )){
                is Result.Success -> {
                    _loadingStatus.value = LoadingStatus.Success
                    _userProfile.value = result.result.profile
                    prefsUtils.putObject(PrefKeys.USER_PROFILE, result.result)
                    prefsUtils.putString(PrefKeys.LAST_USER_EMAIL, email.value)
                    sharedPrefs.saveIfUserHasLoggedInBefore()
                    getProfessionalList()
                }

                is Result.Error -> {
                    _loadingStatus.value =
                        LoadingStatus.Error(result.errorCode, result.errorMessage)
                }
            }
        }
    }

    fun getProfessionalList() {
//        _loadingStatus.value =
//            LoadingStatus.Loading(resourceProvider.getString(R.string.please_wait))
        val token = prefsUtils.getPrefAsObject(
            PrefKeys.USER_PROFILE,
            LoginSignUpResponse::class.java
        ).tokenData.token
        val bearer = "Bearer ".plus(token)
        viewModelScope.launch {
            when (val r = authRepository.getProfessionalsList(bearer)) {
                is Result.Success -> {
                    Timber.e("successful got here ")
                    prefsUtils.putList(PrefKeys.PROFESSIONS_LIST, r.result)
                    _listOfProfessionals.value = r.result
                   // _loadingStatus.value = LoadingStatus.Success
                }
                is Result.Error -> {
                    Timber.e(r.errorMessage)
                  //  _loadingStatus.value = LoadingStatus.Error(r.errorCode, r.errorMessage)
                }
            }
        }

    }

    override fun addAllLiveDataToObservablesList() {
        observablesList.add(userProfile)
    }
}