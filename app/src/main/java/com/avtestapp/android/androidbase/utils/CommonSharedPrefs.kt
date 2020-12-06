package com.avtestapp.android.androidbase.utils

import com.avtestapp.android.androidbase.PrefKeys
import com.avtestapp.android.androidbase.av_test.models.response.LoginSignUpResponse
import com.avtestapp.android.androidbase.av_test.models.response.ProfileResponse
import javax.inject.Inject

class CommonSharedPrefs @Inject constructor(val prefsUtils: PrefsUtils) {

    fun saveIfUserHasLoggedInBefore(userLoggedInBefore: Boolean = true) {
        if (prefsUtils.doesContain(PrefKeys.HAS_LOGGED_IN_BEFORE)) {
            prefsUtils.removePref(PrefKeys.HAS_LOGGED_IN_BEFORE)
        }
        prefsUtils.putBoolean(PrefKeys.HAS_LOGGED_IN_BEFORE, userLoggedInBefore)
     }

    fun getIfUserHasLoggedInBefore(): Boolean =
        prefsUtils.getBoolean(PrefKeys.HAS_LOGGED_IN_BEFORE, false)

    fun getUserProfile(fromJustProfile: Boolean = false): ProfileResponse? {
        return if (fromJustProfile && prefsUtils.doesContain(PrefKeys.USER_PROFILE_SINGLE )) {
            prefsUtils.getPrefAsObject(
                PrefKeys.USER_PROFILE_SINGLE,
                ProfileResponse::class.java
            )
        } else {
            prefsUtils.getPrefAsObject(
                PrefKeys.USER_PROFILE,
                LoginSignUpResponse::class.java
            ).profile
        }
    }

    fun getLastUSerEmail(): String? = prefsUtils.getString(PrefKeys.LAST_USER_EMAIL, "")
}