package com.avtestapp.android.androidbase.av_test.core.change_password

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.avtestapp.android.androidbase.App
import com.avtestapp.android.androidbase.R
import com.avtestapp.android.androidbase.av_test.auth.sign_up.SignUpFragmentDirections
import com.avtestapp.android.androidbase.base.BaseViewModel
import com.avtestapp.android.androidbase.base.BaseViewModelFragment
import com.avtestapp.android.androidbase.databinding.ChangePasswordFragmentBinding
import com.avtestapp.android.androidbase.extensions.setFeedbackSource
import javax.inject.Inject

class ChangePasswordFragment : BaseViewModelFragment() {

    lateinit var binding: ChangePasswordFragmentBinding


    companion object {
        fun newInstance() = ChangePasswordFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ChangePasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ChangePasswordFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (mainActivity.applicationContext as App).component.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ChangePasswordViewModel::class.java)
        binding.viewModel = viewModel
        setupView()
        subscribeObservers()
    }

    private fun setupView(){
        mainActivity.setUpToolBar("Change Password", isRootPage = false)


        binding.changePasswordButton.setOnClickListener {
            viewModel.changePassword()
        }
    }

    private fun subscribeObservers(){
        binding.currentPasswordInputEditText.setFeedbackSource(this, viewModel.oldPassword)
        binding.newPasswordInputEditText.setFeedbackSource(this, viewModel.newPassword)
        binding.retypePasswordInputEditText.setFeedbackSource(this, viewModel.confirmNewPassword)

        viewModel.passwordChangeSuccessful.observe(viewLifecycleOwner, Observer {
            showDialogWithAction(
                title = "Action Successful",
                body = "Your password has been changed successfully",
                positiveAction = {findNavController().navigate(R.id.dashboardFragment)}
            )
        })
    }

    override fun getViewModel() = viewModel
}