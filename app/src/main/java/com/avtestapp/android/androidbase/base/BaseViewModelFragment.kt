package com.avtestapp.android.androidbase.base

import android.net.Uri
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.Observer
import com.avtestapp.android.androidbase.R
import com.avtestapp.android.androidbase.networkutils.LoadingStatus
import com.bumptech.glide.Glide
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
                is LoadingStatus.Loading -> {
                    Timber.e("Loading Loading Loading ..... ${it.message}")
                    mainActivity.showLoading(it.message)
                }
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

    fun displayCircularImage(url: String, imageView: AppCompatImageView) {
        Glide.with(requireActivity())
            .load(url)
            .circleCrop()
            .placeholder(R.drawable.default_rectangle_image)
            .into(imageView)
    }

    abstract fun getViewModel(): BaseViewModel
}