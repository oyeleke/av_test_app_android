package com.avtestapp.android.androidbase.av_test.core.knowledge_score

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avtestapp.android.androidbase.App
import com.avtestapp.android.androidbase.R
import com.avtestapp.android.androidbase.base.BaseFragment
import com.avtestapp.android.androidbase.base.BaseViewModel
import com.avtestapp.android.androidbase.base.BaseViewModelFragment
import com.avtestapp.android.androidbase.databinding.KnowledgeScoreFragmentBinding
import com.avtestapp.android.androidbase.extensions.hide
import com.avtestapp.android.androidbase.utils.CommonSharedPrefs
import javax.inject.Inject

class KnowledgeScoreFragment : BaseViewModelFragment() {

    lateinit var binding : KnowledgeScoreFragmentBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var sharedPrefs: CommonSharedPrefs

    private lateinit var viewModel: KnowledgeScoreViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = KnowledgeScoreFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (mainActivity.applicationContext as App).component.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(KnowledgeScoreViewModel::class.java)
        setupViews()
    }

    private fun setupViews() {
        mainActivity.setUpToolBar("Knowledge Score", isRootPage = false)
        val profile = sharedPrefs.getUserProfile()
        if (!profile?.imageUrl.isNullOrEmpty()){
            displayCircularImage(profile?.imageUrl?:"", binding.userProfilePicture)
            binding.userInitialsText.hide()
        } else {
            val firstInitial = profile?.firstName?.first()
            val secondInitial = profile?.lastName?.first()
            binding.userProfilePicture.hide()
            binding.userInitialsText.text = "$firstInitial$secondInitial"
        }
        binding.userNameTextView.text = "${profile?.firstName} ${profile?.lastName}"
    }

    override fun getViewModel() = viewModel
}