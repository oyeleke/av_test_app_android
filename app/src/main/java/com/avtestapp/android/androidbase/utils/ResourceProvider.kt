package com.avtestapp.android.androidbase.utils

import android.app.Application
import javax.inject.Inject

class ResourceProvider @Inject constructor(private val app: Application) {





    fun getString(resId: Int): String {
        return app.getString(resId)
    }

    fun getString(resId: Int, value: String): String {
        return app.getString(resId, value)
    }
}