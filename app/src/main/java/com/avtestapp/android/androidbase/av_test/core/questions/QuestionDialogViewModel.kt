package com.avtestapp.android.androidbase.av_test.core.questions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.avtestapp.android.androidbase.av_test.models.QuestionTypeWithHash
import com.avtestapp.android.androidbase.av_test.repository.QuestionRepository
import com.avtestapp.android.androidbase.base.BaseViewModel
import com.avtestapp.android.androidbase.extensions.Event
import com.avtestapp.android.androidbase.utils.PrefsUtils
import com.avtestapp.android.androidbase.utils.ResourceProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionDialogViewModel @Inject constructor(
    val prefsUtils: PrefsUtils,
    val resourceProvider: ResourceProvider,
    private val questionRepository: QuestionRepository
) : BaseViewModel() {

    private val _questionTypePickedWithHash = MutableLiveData<Event<QuestionTypeWithHash>>()


    val questionTypePickedWithHash : LiveData<Event<QuestionTypeWithHash>>
        get() = _questionTypePickedWithHash

    fun updateQuestionType(questionTypeWithHash: QuestionTypeWithHash){
        _questionTypePickedWithHash.value = Event(questionTypeWithHash)
    }

    override fun addAllLiveDataToObservablesList() {
        observablesList.addAll(arrayListOf(questionTypePickedWithHash) )
    }
}