package com.avtestapp.android.androidbase.av_test.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.avtestapp.android.androidbase.App
import com.avtestapp.android.androidbase.PrefKeys
import com.avtestapp.android.androidbase.R
import com.avtestapp.android.androidbase.av_test.models.response.LoginSignUpResponse
import com.avtestapp.android.androidbase.base.BaseFragment
import com.avtestapp.android.androidbase.databinding.FragmentOnboardingStartBinding
import com.avtestapp.android.androidbase.utils.PrefsUtils
import javax.inject.Inject


class OnboardingStartFragment : BaseFragment() {

    lateinit var binding: FragmentOnboardingStartBinding


    @Inject
    lateinit var prefsUtils: PrefsUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnboardingStartBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (mainActivity.applicationContext as App).component.inject(this)
        binding.letsDoitButton.setOnClickListener {
            findNavController().navigate(OnboardingStartFragmentDirections.actionOnboardingStartFragmentToUploadImageFragment())
        }

        val user = prefsUtils.getPrefAsObject(PrefKeys.USER_PROFILE, LoginSignUpResponse::class.java)

        binding.text3Bold.text = "Welcome, ${user.profile.firstName}"
    }


}