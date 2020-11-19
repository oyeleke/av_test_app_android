package com.avtestapp.android.androidbase.av_test.auth.forgot_password

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.avtestapp.android.androidbase.App
import com.avtestapp.android.androidbase.R
import com.avtestapp.android.androidbase.av_test.auth.sign_up.SignUpFragmentDirections
import com.avtestapp.android.androidbase.base.BaseViewModel
import com.avtestapp.android.androidbase.base.BaseViewModelFragment
import com.avtestapp.android.androidbase.databinding.ForgotPasswordFragmentBinding
import com.avtestapp.android.androidbase.extensions.setFeedbackSource
import javax.inject.Inject

class ForgotPasswordFragment : BaseViewModelFragment() {

    private lateinit var binding: ForgotPasswordFragmentBinding


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ForgotPasswordFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (mainActivity.applicationContext as App).component.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ForgotPasswordViewModel::class.java)
        binding.viewModel = viewModel
        subscribeObservers()
        setUpViews()
    }

    private fun setUpViews() {
        binding.contentFragmentForgotPassword.signInButton.setOnClickListener {
            viewModel.initializePassword()
        }
    }

    private fun subscribeObservers() {
        viewModel.passwordResetResponseObserver.observe(viewLifecycleOwner, Observer {
            showDialogWithAction(
                title = "reset password",
                body = "Dear ${it.firstName} your application was successful, a mail has been sent to ${it.email}, please follow the instructions.",
                positiveAction = {
                    findNavController().navigate(
                        ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToVerificationFragment(
                            true
                        )
                    )
                }
            )
        })
        binding.contentFragmentForgotPassword.emailTextInputEditText.setFeedbackSource(this, viewModel.email)
    }


    override fun getViewModel(): BaseViewModel = viewModel
}