package com.avtestapp.android.androidbase.av_test.modals

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.avtestapp.android.androidbase.App
import com.avtestapp.android.androidbase.PrefKeys
import com.avtestapp.android.androidbase.UI_CONSTANTS
import com.avtestapp.android.androidbase.av_test.core.questions.QuestionDialogViewModel
import com.avtestapp.android.androidbase.av_test.core.questions.QuestionViewModel
import com.avtestapp.android.androidbase.av_test.models.QuestionType
import com.avtestapp.android.androidbase.av_test.models.QuestionTypeWithHash
import com.avtestapp.android.androidbase.av_test.models.response.ProfessionItemsResponse
import com.avtestapp.android.androidbase.base.BaseBottomSheetDialogFragment
import com.avtestapp.android.androidbase.databinding.QuestionTypeBottomSheetModalBinding
import com.avtestapp.android.androidbase.extensions.hide
import com.avtestapp.android.androidbase.utils.PrefsUtils
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class QuestionTypeBottomSheetModal : BaseBottomSheetDialogFragment(){

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var prefsUtils: PrefsUtils

    private lateinit var viewModel: QuestionDialogViewModel

    private lateinit var binding: QuestionTypeBottomSheetModalBinding
    companion object{
        fun newInstance(): QuestionTypeBottomSheetModal{
            return QuestionTypeBottomSheetModal()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = QuestionTypeBottomSheetModalBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity().applicationContext as App).component.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(QuestionDialogViewModel::class.java)
        setUpView()
    }

    private fun setUpView(){

        val list = prefsUtils.getList<ProfessionItemsResponse>(PrefKeys.PROFESSIONS_LIST, object : TypeToken<List<ProfessionItemsResponse>>() {}.type)

        val listOfString = Array(list.size){""}
        var i = 0
        list.forEach {
            listOfString[i++] = it.name
        }

        if (!listOfString.contains(UI_CONSTANTS.PILOT)){
            binding.pilotLayout.hide()
        }

        if (!listOfString.contains(UI_CONSTANTS.AIR_DISPATCHER)){
            binding.flightDispatcherLayout.hide()
        }

        if (!listOfString.contains(UI_CONSTANTS.FLIGHT_ATTENDANT)){
            binding.flightAttendantLayout.hide()
        }

        if (!listOfString.contains(UI_CONSTANTS.AIR_TRAFFIC_CONTROLLER)){
            binding.airTrafficControllerLayout.hide()
        }

        if (!listOfString.contains(UI_CONSTANTS.ENGINEER)){
            binding.engineerLayout.hide()
        }

        binding.airTrafficControllerLayout.setOnClickListener {
            viewModel.updateQuestionType(QuestionTypeWithHash(
                questionType = QuestionType.AIR_TRAFFIC_CONTROLLER,
                hashString = getHashWithTitle(UI_CONSTANTS.AIR_TRAFFIC_CONTROLLER, list)?: ""
            ))

            this.dismiss()
        }
        binding.pilotLayout.setOnClickListener {
            viewModel.updateQuestionType(QuestionTypeWithHash(
                questionType = QuestionType.PILOT,
                hashString = getHashWithTitle(UI_CONSTANTS.PILOT, list)?: ""
            ))

            this.dismiss()
        }
        binding.engineerLayout.setOnClickListener {
            viewModel.updateQuestionType(QuestionTypeWithHash(
                questionType = QuestionType.ENGINEER,
                hashString = getHashWithTitle(UI_CONSTANTS.ENGINEER, list)?: ""
            ))

            this.dismiss()
        }

        binding.flightAttendantLayout.setOnClickListener {
            viewModel.updateQuestionType(QuestionTypeWithHash(
                questionType = QuestionType.FLIGHT_ATTENDANT,
                hashString = getHashWithTitle(UI_CONSTANTS.FLIGHT_ATTENDANT, list)?: ""
            ))

            this.dismiss()
        }
        binding.flightDispatcherLayout.setOnClickListener {
            viewModel.updateQuestionType(QuestionTypeWithHash(
                questionType = QuestionType.AIR_DISPATCHER,
                hashString = getHashWithTitle(UI_CONSTANTS.AIR_DISPATCHER, list)?: ""
            ))
            this.dismiss()
        }
    }



    private fun getHashWithTitle(professionTitle: String, professionItemsResponseList: ArrayList<ProfessionItemsResponse>): String?{
        val item = professionItemsResponseList.find {
             it.name == professionTitle
        }

        return item?.id
    }


}