package com.avtestapp.android.androidbase.av_test.core.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.avtestapp.android.androidbase.Interactors.LoginUser
import com.avtestapp.android.androidbase.PrefKeys
import com.avtestapp.android.androidbase.R
import com.avtestapp.android.androidbase.av_test.models.response.LoginSignUpResponse
import com.avtestapp.android.androidbase.av_test.models.response.ProfileResponse
import com.avtestapp.android.androidbase.av_test.repository.AuthRepository
import com.avtestapp.android.androidbase.base.BaseViewModel
import com.avtestapp.android.androidbase.extensions.Event
import com.avtestapp.android.androidbase.networkutils.LoadingStatus
import com.avtestapp.android.androidbase.networkutils.Result
import com.avtestapp.android.androidbase.utils.PrefsUtils
import com.avtestapp.android.androidbase.utils.ResourceProvider
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    val loginUser: LoginUser,
    val authRepository: AuthRepository,
    val prefsUtils: PrefsUtils,
    val resourceProvider: ResourceProvider
) : BaseViewModel() {
    private val _userProfileGotten = MutableLiveData<Event<ProfileResponse>>()

    val userProfileGotten: LiveData<Event<ProfileResponse>>
        get() = _userProfileGotten

    override fun addAllLiveDataToObservablesList() {
        observablesList.add(userProfileGotten)
    }

    fun getUser() {
        _loadingStatus.value =
            LoadingStatus.Loading(resourceProvider.getString(R.string.please_wait))
        val token = prefsUtils.getPrefAsObject(
            PrefKeys.USER_PROFILE,
            LoginSignUpResponse::class.java
        ).tokenData.token
        val bearer = "Bearer ".plus(token)
        viewModelScope.launch {
            when (val r = authRepository.getUser(bearer)) {
                is Result.Success -> {
                    Timber.e("successful got here ")
                    _userProfileGotten.value = Event(r.result)
                    prefsUtils.putObject(PrefKeys.USER_PROFILE_SINGLE, r.result)
                    _loadingStatus.value = LoadingStatus.Success
                }
                is Result.Error -> {
                    Timber.e(r.errorMessage)
                    _loadingStatus.value = LoadingStatus.Error(r.errorCode, r.errorMessage)
                }
            }
        }

    }
}