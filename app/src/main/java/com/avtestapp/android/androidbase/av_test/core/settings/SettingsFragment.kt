package com.avtestapp.android.androidbase.av_test.core.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.avtestapp.android.androidbase.App
import com.avtestapp.android.androidbase.base.BaseViewModelFragment
import com.avtestapp.android.androidbase.databinding.SettingsFragmentBinding
import com.avtestapp.android.androidbase.extensions.hide
import com.avtestapp.android.androidbase.extensions.show
import com.avtestapp.android.androidbase.utils.CommonSharedPrefs
import javax.inject.Inject

class SettingsFragment : BaseViewModelFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var sharedPrefs: CommonSharedPrefs

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
        viewModel.getUser()
    }

    private fun subscribeObservers() {
        viewModel.userProfileGotten.observe(viewLifecycleOwner, Observer {
            it.peekContent()?.let {profile ->
                if (!profile?.imageUrl.isNullOrEmpty()) {
                    displayCircularImage(profile?.imageUrl ?: "", binding.customerImageView)
                    binding.userInitialsText.hide()
                    binding.customerImageView.show()
                } else {
                    val firstInitial = profile?.firstName?.first()
                    val secondInitial = profile?.lastName?.first()
                    binding.customerImageView.hide()
                    binding.userInitialsText.show()
                    binding.userInitialsText.text = "$firstInitial$secondInitial"
                }
                binding.userNameEmailTextView.text = "${profile?.email}"
                binding.userNameTextView.text = "${profile?.firstName} ${profile?.lastName}"
            }
        })
    }

    private fun setupViews() {

        mainActivity.setUpToolBar("Settings", isRootPage = true)

        binding.editProfileButton.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToEditProfileFragment())
        }

        val profile = sharedPrefs.getUserProfile(true)
        if (!profile?.imageUrl.isNullOrEmpty()) {
            displayCircularImage(profile?.imageUrl ?: "", binding.customerImageView)
            binding.userInitialsText.hide()
            binding.customerImageView.show()
        } else {
            val firstInitial = profile?.firstName?.first()
            val secondInitial = profile?.lastName?.first()
            binding.customerImageView.hide()
            binding.userInitialsText.show()
            binding.userInitialsText.text = "$firstInitial$secondInitial"
        }
        binding.userNameEmailTextView.text = "${profile?.email}"
        binding.userNameTextView.text = "${profile?.firstName} ${profile?.lastName}"

        binding.studyQuestionCorrectQuestionSwitch.isChecked = sharedPrefs.getShowOnlyAnswersForStudyQuestions()
        binding.practiceQuestionShuffleModeSwitch.isChecked = sharedPrefs.getIfUserWantsPracticeQuestionsShuffled()
        binding.studyQuestionShuffleModeSwitch.isChecked = sharedPrefs.getIfUserWantsStudyQuestionsShuffled()


        binding.studyQuestionCorrectQuestionSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefs.showOnlyAnswersForStudySessions(isChecked)
        }

        binding.practiceQuestionShuffleModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefs.shufflePracticeQuestions(isChecked)
        }

        binding.studyQuestionShuffleModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefs.shuffleStudyQuestions(isChecked)
        }

        binding.changePassword.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToChangePasswordFragment())
        }
    }

}