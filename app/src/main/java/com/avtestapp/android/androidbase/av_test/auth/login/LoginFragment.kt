package com.avtestapp.android.androidbase.av_test.auth.login

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
import com.avtestapp.android.androidbase.databinding.LoginFragmentBinding
import com.avtestapp.android.androidbase.extensions.setFeedbackSource
import javax.inject.Inject

class LoginFragment : BaseViewModelFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var binding: LoginFragmentBinding


    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (mainActivity.applicationContext as App).component.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
        binding.viewModel = viewModel
        setUpViews()
        subscribeObservables()
    }


    fun setUpViews(){
        binding.contentFragmentLogin.signInButton.setOnClickListener {
            viewModel.logUserIn()
            //findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToDashboardFragment())
        }

        binding.contentFragmentLogin.cantSignInText.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment())
        }

        binding.contentFragmentLogin.signUpLink.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
        }
    }
    fun subscribeObservables(){
        binding.contentFragmentLogin.emailTextInputEditText.setFeedbackSource(this, viewModel.email)
        binding.contentFragmentLogin.passwordTextInputEditText.setFeedbackSource(this, viewModel.password)

        viewModel.userProfile.observe(viewLifecycleOwner, Observer {
            if (!it.verified){
                showDialogWithAction(
                    title = "Not verified",
                    body = "Dear ${it.firstName} please check your mail ${it.email}, to verify your account",
                    positiveAction = { findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToVerificationFragment(false))}
                )
            } else {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToDashboardFragment())
            }
        })
    }
    override fun getViewModel(): BaseViewModel = viewModel

}