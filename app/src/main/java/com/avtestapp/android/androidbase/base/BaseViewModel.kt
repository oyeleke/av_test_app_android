/**
 * Copyright (c) 2019 Cotta & Cush Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.avtestapp.android.androidbase.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.avtestapp.android.androidbase.networkutils.LoadingStatus
import com.avtestapp.android.androidbase.utils.CommonSharedPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject

abstract class BaseViewModel() : ViewModel() {

    val observablesList: MutableList<LiveData<*>> = mutableListOf()

    @Inject
    lateinit var sharedPrefs: CommonSharedPrefs

    private val viewModelBackgroundJob = SupervisorJob()
    protected val viewModelIOScope = CoroutineScope(viewModelBackgroundJob + Dispatchers.IO)

    protected val _loadingStatus = MutableLiveData<LoadingStatus>()

    val loadingStatus: LiveData<LoadingStatus>
        get() = _loadingStatus

    fun errorShown() {
        _loadingStatus.value = null
    }

    /**
     * We want to add all observables to the list so that they are removed when the view is destroyed
     * If this isn't done, we would have multiple observers on the same variable which might lead to a crash
     */
    abstract fun addAllLiveDataToObservablesList()

    override fun onCleared() {
        super.onCleared()
        viewModelBackgroundJob.cancel()
    }
}
