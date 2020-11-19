package com.avtestapp.android.androidbase.av_test.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.avtestapp.android.androidbase.PrefKeys
import com.avtestapp.android.androidbase.R
import com.avtestapp.android.androidbase.av_test.models.requests.OnboardUserRequest
import com.avtestapp.android.androidbase.av_test.models.response.LoginSignUpResponse
import com.avtestapp.android.androidbase.av_test.models.response.ProfessionItemsResponse
import com.avtestapp.android.androidbase.av_test.repository.AuthRepository
import com.avtestapp.android.androidbase.base.BaseViewModel
import com.avtestapp.android.androidbase.extensions.Event
import com.avtestapp.android.androidbase.networkutils.LoadingStatus
import com.avtestapp.android.androidbase.networkutils.Result
import com.avtestapp.android.androidbase.utils.Form
import com.avtestapp.android.androidbase.utils.FormField
import com.avtestapp.android.androidbase.utils.FormFieldValidators.validateNotEmpty
import com.avtestapp.android.androidbase.utils.FormFieldValidators.validateProfessionalItem
import com.avtestapp.android.androidbase.utils.PrefsUtils
import com.avtestapp.android.androidbase.utils.ResourceProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

class Onboarding3ViewModel @Inject constructor(
    val authRepository: AuthRepository,
    val prefsUtils: PrefsUtils,
    val resourceProvider: ResourceProvider
) : BaseViewModel() {

    private val _listOfProfessionals = MutableLiveData<List<ProfessionItemsResponse>>()
    val listOfProfessionals: LiveData<List<ProfessionItemsResponse>>
        get() = _listOfProfessionals

    private val _onboardingSuccessful = MutableLiveData<Event<Boolean>>()

    val onboardingSuccessful: LiveData<Event<Boolean>>
        get() = _onboardingSuccessful



    val onboardingForm = Form()

    var profession = FormField<ProfessionItemsResponse>().apply {
        addValidator { validateProfessionalItem() }
    }

    var licsenseNo = FormField<String>(required = false).apply {
        addValidator { validateNotEmpty() }
    }

    var nationality = FormField<String>().apply {
        addValidator { validateNotEmpty() }
    }

    var professionSpinner = FormField<ProfessionItemsResponse>().apply {
        addValidator { validateProfessionalItem() }
        addTo(onboardingForm)
    }

    var nationalitySpinner  = FormField<String>().apply {
        addValidator { validateNotEmpty() }
    }


    fun getProfessionalList() {
        _loadingStatus.value =
            LoadingStatus.Loading(resourceProvider.getString(R.string.please_wait))
        val token = prefsUtils.getPrefAsObject(
            PrefKeys.USER_RESPONSE,
            LoginSignUpResponse::class.java
        ).tokenData.token
        val bearer = "Bearer ".plus(token)
        viewModelScope.launch {
            when (val r = authRepository.getProfessionalsList(bearer)) {
                is Result.Success -> {
                    _listOfProfessionals.value = r.result
                    _loadingStatus.value = LoadingStatus.Success
                }
                is Result.Error -> {
                    _loadingStatus.value = LoadingStatus.Error(r.errorCode, r.errorMessage)
                }
            }
        }

    }

    fun onBoardUser() {
        if (!onboardingForm.verify()) return

        _loadingStatus.value =
            LoadingStatus.Loading(resourceProvider.getString(R.string.please_wait))

        val token = prefsUtils.getPrefAsObject(
            PrefKeys.USER_RESPONSE,
            LoginSignUpResponse::class.java
        ).tokenData.token
        val bearer = "Bearer ".plus(token)
        viewModelScope.launch {
            when (val r = authRepository.onboardUser(
                bearer, OnboardUserRequest(
                    licenseNumber = licsenseNo.value!!,
                    nationality = nationalitySpinner.value!!
                )
            )) {
                is Result.Success -> {
                    _onboardingSuccessful.value = Event(true)
                    _loadingStatus.value = LoadingStatus.Success
                }
                is Result.Error -> {
                    _loadingStatus.value = LoadingStatus.Error(r.errorCode, r.errorMessage)
                }
            }
        }
    }

    override fun addAllLiveDataToObservablesList() {
        observablesList.add(listOfProfessionals)
    }
    // TODO: Implement the ViewModel
}