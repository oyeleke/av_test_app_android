package com.avtestapp.android.androidbase.av_test.core.settings

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.avtestapp.android.androidbase.App
import com.avtestapp.android.androidbase.R
import com.avtestapp.android.androidbase.base.BaseViewModel
import com.avtestapp.android.androidbase.base.BaseViewModelFragment
import com.avtestapp.android.androidbase.databinding.SettingsFragmentBinding
import javax.inject.Inject

class SettingsFragment : BaseViewModelFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: SettingsViewModel

    private lateinit var binding: SettingsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SettingsFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun getViewModel() = viewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (mainActivity.applicationContext as App).component.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SettingsViewModel::class.java)
        setupViews()
        subscribeObservers()
    }

    private fun subscribeObservers() {

    }

    private fun setupViews() {

        mainActivity.setUpToolBar("Setting", isRootPage = true)

        binding.editProfileButton.setOnClickListener{
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToEditProfileFragment())
        }
    }

}