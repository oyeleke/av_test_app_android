package com.avtestapp.android.androidbase.av_test.auth.reset_password

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
import com.avtestapp.android.androidbase.databinding.ResetPasswordFragmentBinding
import com.avtestapp.android.androidbase.extensions.setFeedbackSource
import javax.inject.Inject

class ResetPasswordFragment : BaseViewModelFragment() {

    companion object {
        fun newInstance() = ResetPasswordFragment()
    }


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ResetPasswordViewModel
    private lateinit var binding: ResetPasswordFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ResetPasswordFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (mainActivity.applicationContext as App).component.inject(this)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(ResetPasswordViewModel::class.java)
        binding.viewModel = viewModel
        setUpObservers()
        setUpViews()
    }

    private fun setUpViews() {
        binding.contentFragmentResetPassword.resetPasswordButton.setOnClickListener {
            viewModel.resetPassword()
        }
    }

    private fun setUpObservers() {
        viewModel.observePasswordSuccess.observe(viewLifecycleOwner, Observer {
            if (it) {
                showDialogWithAction(
                    title = "Password reset Successful",
                    body = "You can log in with your password now",
                    positiveAction = {
                        findNavController().navigate(ResetPasswordFragmentDirections.actionResetPasswordFragmentToLoginFragment())
                    }
                )
            }
        })

        binding.contentFragmentResetPassword.password1TextInputEditText.setFeedbackSource(this, viewModel.password1)
        binding.contentFragmentResetPassword.password2TextInputEditText.setFeedbackSource(this, viewModel.password2)
    }


    override fun getViewModel(): BaseViewModel = viewModel
}