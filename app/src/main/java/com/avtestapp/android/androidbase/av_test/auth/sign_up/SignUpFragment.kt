package com.avtestapp.android.androidbase.av_test.auth.sign_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.avtestapp.android.androidbase.App
import com.avtestapp.android.androidbase.R
import com.avtestapp.android.androidbase.base.BaseViewModel
import com.avtestapp.android.androidbase.base.BaseViewModelFragment
import com.avtestapp.android.androidbase.databinding.SignUpFragmentBinding
import com.avtestapp.android.androidbase.extensions.setFeedbackSource
import javax.inject.Inject

class SignUpFragment : BaseViewModelFragment() {



    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: SignUpViewModel
    lateinit var binding: SignUpFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SignUpFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return  binding.root
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (mainActivity.applicationContext as App).component.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SignUpViewModel::class.java)
        binding.viewModel = viewModel
        setUpView()
        subScribeObservers()
    }

    fun setUpView(){
        binding.contentFragmentSignUp.loginInsteadTextView.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
        }

        binding.contentFragmentSignUp.signUpButton.setOnClickListener {
            viewModel.signUpUser()
            //findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToVerificationFragment(false))
        }

    }
    fun subScribeObservers(){
        viewModel.userProfile.observe(viewLifecycleOwner, Observer {
            showDialogWithAction(
                title = "Registration Successful",
                body = "Dear ${it.profile.firstName} your registration was successful, a mail has been sent to ${it.profile.email}, please follow the instructions.",
                positiveAction = {findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToVerificationFragment(false))}
            )
        } )

        binding.contentFragmentSignUp.firstNameTextInputEditText.setFeedbackSource(this, viewModel.firstName)
        binding.contentFragmentSignUp.lastNameTextInputEditText.setFeedbackSource(this, viewModel.lastName)
        binding.contentFragmentSignUp.emailTextInputEditText.setFeedbackSource(this, viewModel.email)
        binding.contentFragmentSignUp.passwordTextInputEditText.setFeedbackSource(this, viewModel.password)

    }

    override fun getViewModel(): BaseViewModel = viewModel

}