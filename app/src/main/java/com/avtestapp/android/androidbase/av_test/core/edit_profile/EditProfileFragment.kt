package com.avtestapp.android.androidbase.av_test.core.edit_profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avtestapp.android.androidbase.App
import com.avtestapp.android.androidbase.R
import com.avtestapp.android.androidbase.base.BaseViewModel
import com.avtestapp.android.androidbase.base.BaseViewModelFragment
import javax.inject.Inject

class EditProfileFragment : BaseViewModelFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: EditProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_profile_fragment, container, false)
    }

    override fun getViewModel() = viewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (mainActivity.applicationContext as App).component.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(EditProfileViewModel::class.java)
        setupView()
    }

    private fun setupView() {
        mainActivity.setUpToolBar("Edit Profile", isRootPage = false)
    }

}