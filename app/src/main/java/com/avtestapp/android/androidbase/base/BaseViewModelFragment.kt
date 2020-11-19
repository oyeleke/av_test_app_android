package com.avtestapp.android.androidbase.base

import androidx.lifecycle.Observer
import com.avtestapp.android.androidbase.networkutils.LoadingStatus
import timber.log.Timber

abstract class BaseViewModelFragment : BaseFragment() {

    override fun onStart() {
        super.onStart()
        if (getViewModel().loadingStatus.hasObservers()) return
        Timber.d("Super Loading Status is observed")
        getViewModel().loadingStatus.observe(this, Observer {
            when (it) {
                LoadingStatus.Success -> {
                    mainActivity.dismissLoading()
                    Timber.e("I, \"mainActivity.dismissLoading\", can testify that I have been called")

                }
                is LoadingStatus.Loading -> mainActivity.showLoading(it.message)
                is LoadingStatus.Error -> {
                    Timber.d("I, \"mainActivity.showError\", can testify that I have been called")
                    mainActivity.showError(it.errorMessage)
                    getViewModel().errorShown()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        getViewModel().addAllLiveDataToObservablesList()
        for (liveData in getViewModel().observablesList) liveData.removeObservers(this)
    }

    abstract fun getViewModel(): BaseViewModel
}