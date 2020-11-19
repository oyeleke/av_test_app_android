package com.avtestapp.android.androidbase.av_test.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.avtestapp.android.androidbase.App
import com.avtestapp.android.androidbase.PrefKeys
import com.avtestapp.android.androidbase.R
import com.avtestapp.android.androidbase.av_test.CustomSpinnerAdapter
import com.avtestapp.android.androidbase.av_test.models.response.ProfessionItemsResponse
import com.avtestapp.android.androidbase.base.BaseViewModel
import com.avtestapp.android.androidbase.base.BaseViewModelFragment
import com.avtestapp.android.androidbase.databinding.Onboarding3FragmentBinding
import com.avtestapp.android.androidbase.extensions.setFeedbackSource
import com.avtestapp.android.androidbase.utils.Listable
import com.avtestapp.android.androidbase.utils.PrefsUtils
import timber.log.Timber
import javax.inject.Inject

class Onboarding3Fragment : BaseViewModelFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var prefsUtils: PrefsUtils

    private lateinit var viewModel: Onboarding3ViewModel

    private lateinit var binding: Onboarding3FragmentBinding

    private val listOfCountries =
        arrayListOf("Nationality", "Nigeria", "Ghana", "Liberia", "Congo", "Uganda", "South Africa")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = Onboarding3FragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (mainActivity.applicationContext as App).component.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(Onboarding3ViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.getProfessionalList()
        subscribeObservables()
        setUpViews()
    }

    fun subscribeObservables() {
        binding.licenseNumberInputEditText.setFeedbackSource(this, viewModel.licsenseNo)
        binding.professionDropDown.setFeedbackSource(this, viewModel.profession)
        binding.nationalityDropDown.setFeedbackSource(this, viewModel.nationality)

        viewModel.listOfProfessionals.observe(viewLifecycleOwner, Observer {
            prefsUtils.putList(PrefKeys.PROFESSIONS_LIST, it)
            binding.professionDropDown.setItems(it.map { profession ->
                object : Listable<ProfessionItemsResponse> {
                    override val obj = profession

                    override val label = profession.name
                }
            })
        })

        viewModel.listOfProfessionals.observe(viewLifecycleOwner, Observer {
            val hint = ProfessionItemsResponse("", "What is your profession", 2000)
            val list = it.toMutableList()
            list.add(0, hint)
            CustomSpinnerAdapter(
                activity!!,
                R.layout.custom_spinner_text_view,
                list
            ).also { arrayAdapter ->
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerProfession.adapter = arrayAdapter
            }

        })

        viewModel.onboardingSuccessful.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                findNavController().navigate(Onboarding3FragmentDirections.actionOnboarding3FragmentToDashboardFragment())
            }
        })

        viewModel.professionSpinner.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Timber.e(it.name)
            }
        })
    }

    private fun setUpViews() {

        binding.nationalityDropDown.setItems(listOfCountries.map { country ->
            object : Listable<String> {
                override val obj = country
                override val label = country
            }
        })

        CustomSpinnerAdapter(
            activity!!,
            R.layout.custom_spinner_text_view,
            listOfCountries
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerNationality.adapter = arrayAdapter
        }

        binding.spinnerNationality.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    viewModel.nationalitySpinner.value = listOfCountries[position]
                }
            }
        }

        binding.spinnerProfession.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    viewModel.professionSpinner.value =
                        viewModel.listOfProfessionals.value?.get(position - 1)
                }
            }
        }

        binding.doneButton.setOnClickListener {
            viewModel.onBoardUser()
            //findNavController().navigate(Onboarding3FragmentDirections.actionOnboarding3FragmentToDashboardFragment())
        }
    }

    override fun getViewModel(): BaseViewModel = viewModel
}