package com.avtestapp.android.androidbase.av_test.auth.verification

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.avtestapp.android.androidbase.App
import com.avtestapp.android.androidbase.R
import com.avtestapp.android.androidbase.base.BaseViewModel
import com.avtestapp.android.androidbase.base.BaseViewModelFragment
import com.avtestapp.android.androidbase.databinding.VerificationFragmentBinding
import com.avtestapp.android.androidbase.extensions.hide
import com.avtestapp.android.androidbase.extensions.setFeedbackSource
import javax.inject.Inject

class VerificationFragment : BaseViewModelFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var binding: VerificationFragmentBinding
    companion object {
        fun newInstance() = VerificationFragment()
    }



    private lateinit var viewModel: VerificationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = VerificationFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }



    override fun getViewModel(): BaseViewModel = viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (mainActivity.applicationContext as App).component.inject(this)


        viewModel = ViewModelProvider(this, viewModelFactory).get(VerificationViewModel::class.java)
        binding.viewModel = viewModel
        var isFromForgetFragment = VerificationFragmentArgs.fromBundle(arguments!!).isForgotPassword
        if (isFromForgetFragment){
            setUpVerificationForForgotPassword()
        }
        setUpObservables()
        setUpView(isFromForgetFragment)
    }

    private fun setUpView(isFromForgotPassword: Boolean) {



        binding.contentFragmentVerification.signInButton.setOnClickListener {
            if (isFromForgotPassword){
                viewModel.verifyCodeResetPassword()
            } else {
                viewModel.verifyRegistrationCode()
            }
        }

        binding.contentFragmentVerification.otp1TextInputEditText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.contentFragmentVerification.otp1TextInputEditText.clearFocus()
                binding.contentFragmentVerification.otp2TextInputEditText.requestFocus()
                binding.contentFragmentVerification.otp2TextInputEditText.isCursorVisible = true
            }
        })

        binding.contentFragmentVerification.otp2TextInputEditText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.contentFragmentVerification.otp2TextInputEditText.clearFocus()
                binding.contentFragmentVerification.otp3TextInputEditText.requestFocus()
                binding.contentFragmentVerification.otp3TextInputEditText.isCursorVisible = true
            }
        })

        binding.contentFragmentVerification.otp3TextInputEditText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.contentFragmentVerification.otp3TextInputEditText.clearFocus()
                binding.contentFragmentVerification.otp4TextInputEditText.requestFocus()
                binding.contentFragmentVerification.otp4TextInputEditText.isCursorVisible = true
            }
        })
    }

    private fun setUpObservables(){
        viewModel.verifyRegistration.observe(viewLifecycleOwner, Observer{
            if (it){
                findNavController().navigate(VerificationFragmentDirections.actionVerificationFragmentToOnboardingStartFragment())
            }
        })

        viewModel.verifyPasswordResetCode.observe(viewLifecycleOwner, Observer {
            if (it){
                findNavController().navigate(VerificationFragmentDirections.actionVerificationFragmentToResetPasswordFragment())
            }
        })

        binding.contentFragmentVerification.otp1TextInputEditText.setFeedbackSource(this, viewModel.code1)
        binding.contentFragmentVerification.otp2TextInputEditText.setFeedbackSource(this, viewModel.code2)
        binding.contentFragmentVerification.otp3TextInputEditText.setFeedbackSource(this, viewModel.code3)
        binding.contentFragmentVerification.otp4TextInputEditText.setFeedbackSource(this, viewModel.code4)
    }

    private fun setUpVerificationForForgotPassword(){
        binding.verificationFragmentImageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.forgot_password_logo))
        binding.text1Bold.text = "Verify it's you"
        binding.textTwoLight.text = "Please enter the code received."
        binding.contentFragmentVerification.changeEmailAddressText.hide()
        binding.contentFragmentVerification.didNtReceiveOtpText.text = "Didn't get the code?"
        binding.contentFragmentVerification.resendOtpText.text = "Try again"
    }

}