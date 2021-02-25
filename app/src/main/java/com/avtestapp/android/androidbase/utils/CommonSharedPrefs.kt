package com.avtestapp.android.androidbase.utils

import com.avtestapp.android.androidbase.PrefKeys
import com.avtestapp.android.androidbase.av_test.models.BookMarkedQuestions
import com.avtestapp.android.androidbase.av_test.models.CurrentSessionDetails
import com.avtestapp.android.androidbase.av_test.models.CurrentSessionQuestions
import com.avtestapp.android.androidbase.av_test.models.response.LoginSignUpResponse
import com.avtestapp.android.androidbase.av_test.models.response.Option
import com.avtestapp.android.androidbase.av_test.models.response.ProfileResponse
import java.util.*
import javax.inject.Inject

class CommonSharedPrefs @Inject constructor(val prefsUtils: PrefsUtils) {



    fun saveIfUserHasLoggedInBefore(userLoggedInBefore: Boolean = true) {
        if (prefsUtils.doesContain(PrefKeys.HAS_LOGGED_IN_BEFORE)) {
            prefsUtils.removePref(PrefKeys.HAS_LOGGED_IN_BEFORE)
        }
        prefsUtils.putBoolean(PrefKeys.HAS_LOGGED_IN_BEFORE, userLoggedInBefore)
    }

    fun getIfUserHasLoggedInBefore(): Boolean =
        prefsUtils.getBoolean(PrefKeys.HAS_LOGGED_IN_BEFORE, false)

    fun getUserProfile(fromJustProfile: Boolean = false): ProfileResponse? {
        return if (fromJustProfile && prefsUtils.doesContain(PrefKeys.USER_PROFILE_SINGLE)) {
            prefsUtils.getPrefAsObject(
                PrefKeys.USER_PROFILE_SINGLE,
                ProfileResponse::class.java
            )
        } else {
            prefsUtils.getPrefAsObject(
                PrefKeys.USER_PROFILE,
                LoginSignUpResponse::class.java
            ).profile
        }
    }

    fun getLastUSerEmail(): String? = prefsUtils.getString(PrefKeys.LAST_USER_EMAIL, "")

    fun shufflePracticeQuestions(shufflePracticeQuestions: Boolean) {
        if (prefsUtils.doesContain(PrefKeys.PRACTICE_SHUFFLE_QUESTIONS)) {
            prefsUtils.removePref(PrefKeys.PRACTICE_SHUFFLE_QUESTIONS)
        }
        prefsUtils.putBoolean(PrefKeys.PRACTICE_SHUFFLE_QUESTIONS, shufflePracticeQuestions)
    }

    fun getIfUserWantsPracticeQuestionsShuffled(): Boolean =
        prefsUtils.getBoolean(PrefKeys.PRACTICE_SHUFFLE_QUESTIONS, false)

    fun shuffleStudyQuestions(shuffleStudyQuestions: Boolean) {
        if (prefsUtils.doesContain(PrefKeys.STUDY_SHUFFLE_QUESTIONS)) {
            prefsUtils.removePref(PrefKeys.STUDY_SHUFFLE_QUESTIONS)
        }
        prefsUtils.putBoolean(PrefKeys.STUDY_SHUFFLE_QUESTIONS, shuffleStudyQuestions)
    }

    fun getIfUserWantsStudyQuestionsShuffled(): Boolean =
        prefsUtils.getBoolean(PrefKeys.STUDY_SHUFFLE_QUESTIONS, false)

    fun showOnlyAnswersForStudySessions(showOnlyAnswer: Boolean) {
        if (prefsUtils.doesContain(PrefKeys.SHOW_ONLY_ANSWERS_FOR_STUDY_QUESTIONS)) {
            prefsUtils.removePref(PrefKeys.SHOW_ONLY_ANSWERS_FOR_STUDY_QUESTIONS)
        }
        prefsUtils.putBoolean(PrefKeys.SHOW_ONLY_ANSWERS_FOR_STUDY_QUESTIONS, showOnlyAnswer)
    }

    fun getShowOnlyAnswersForStudyQuestions(): Boolean =
        prefsUtils.getBoolean(PrefKeys.SHOW_ONLY_ANSWERS_FOR_STUDY_QUESTIONS, false)


    fun saveCurrentSession(currentSessionQuestions: CurrentSessionQuestions) {
        if (prefsUtils.doesContain(PrefKeys.CURRENT_QUESTIONS_SESSIONS)) {
            prefsUtils.removePref(PrefKeys.CURRENT_QUESTIONS_SESSIONS)
        }
        prefsUtils.putObject(PrefKeys.CURRENT_QUESTIONS_SESSIONS, currentSessionQuestions)
    }

    fun getCurrentSession(): CurrentSessionQuestions? =
        prefsUtils.getPrefAsObject(
            PrefKeys.CURRENT_QUESTIONS_SESSIONS,
            CurrentSessionQuestions::class.java
        )
    fun saveCurrentStudySession(currentSessionQuestions: CurrentSessionQuestions) {
        if (prefsUtils.doesContain(PrefKeys.CURRENT_STUDY_QUESTIONS_SESSIONS)) {
            prefsUtils.removePref(PrefKeys.CURRENT_STUDY_QUESTIONS_SESSIONS)
        }
        prefsUtils.putObject(PrefKeys.CURRENT_STUDY_QUESTIONS_SESSIONS, currentSessionQuestions)
    }

    fun getCurrentStudySession(): CurrentSessionQuestions? =
        prefsUtils.getPrefAsObject(
            PrefKeys.CURRENT_STUDY_QUESTIONS_SESSIONS,
            CurrentSessionQuestions::class.java
        )

    fun saveCurrentSessionDetails(currentSessionDetails: CurrentSessionDetails) {
        if (prefsUtils.doesContain(PrefKeys.CURRENT_QUESTIONS_SESSIONS_DETAILS)) {
            prefsUtils.removePref(PrefKeys.CURRENT_QUESTIONS_SESSIONS_DETAILS)
        }
        prefsUtils.putObject(PrefKeys.CURRENT_QUESTIONS_SESSIONS_DETAILS, currentSessionDetails)
    }

    fun getCurrentSessionDetails(): CurrentSessionDetails? =
        prefsUtils.getPrefAsObject(
            PrefKeys.CURRENT_QUESTIONS_SESSIONS_DETAILS,
            CurrentSessionDetails::class.java
        )

    fun saveCurrentStudySessionDetails(currentSessionDetails: CurrentSessionDetails) {
        if (prefsUtils.doesContain(PrefKeys.CURRENT_STUDY_QUESTIONS_SESSIONS_DETAILS)) {
            prefsUtils.removePref(PrefKeys.CURRENT_STUDY_QUESTIONS_SESSIONS_DETAILS)
        }
        prefsUtils.putObject(PrefKeys.CURRENT_STUDY_QUESTIONS_SESSIONS_DETAILS, currentSessionDetails)
    }

    fun getCurrentStudySessionDetails(): CurrentSessionDetails? =
        prefsUtils.getPrefAsObject(
            PrefKeys.CURRENT_STUDY_QUESTIONS_SESSIONS_DETAILS,
            CurrentSessionDetails::class.java
        )

    fun deletePref(vararg keys: String) {
        for (key in keys) {
            prefsUtils.removePref(key)
        }
    }

    fun saveCurrentQuestionsForReview(currentSessionQuestions: CurrentSessionQuestions) {
        if (prefsUtils.doesContain(PrefKeys.CURRENT_REVIEW_QUESTIONS_SESSIONS)) {
            prefsUtils.removePref(PrefKeys.CURRENT_REVIEW_QUESTIONS_SESSIONS)
        }
        prefsUtils.putObject(PrefKeys.CURRENT_REVIEW_QUESTIONS_SESSIONS, currentSessionQuestions)
    }

    fun getCurrentReviewQuestions(): CurrentSessionQuestions? =
        prefsUtils.getPrefAsObject(
            PrefKeys.CURRENT_REVIEW_QUESTIONS_SESSIONS,
            CurrentSessionQuestions::class.java
        )

    fun saveBookmarkedQuestions(bookMarkedQuestions: BookMarkedQuestions) {
        if (prefsUtils.doesContain(PrefKeys.BOOKMARKED_QUESTIONS)) {
            prefsUtils.removePref(PrefKeys.BOOKMARKED_QUESTIONS)
        }
        prefsUtils.putObject(PrefKeys.BOOKMARKED_QUESTIONS, bookMarkedQuestions)
    }


    fun getSavedBookMarked(): BookMarkedQuestions {
        return if (prefsUtils.doesContain(PrefKeys.BOOKMARKED_QUESTIONS)) {
            prefsUtils.getPrefAsObject(
                PrefKeys.BOOKMARKED_QUESTIONS,
                BookMarkedQuestions::class.java
            )
        } else {
            val bookMarkedQuestions = BookMarkedQuestions(ArrayList())
            saveBookmarkedQuestions(bookMarkedQuestions)
            bookMarkedQuestions
        }
    }

    fun savePracticeQuestions(practiceQuestionAnswers: HashMap<String, Option>){

    }

    fun saveQuestionType(questionType: String){
        if (prefsUtils.doesContain(PrefKeys.QUESTION_TYPE)) {
            prefsUtils.removePref(PrefKeys.QUESTION_TYPE)
        }
        prefsUtils.putString(PrefKeys.QUESTION_TYPE, questionType)
    }

    fun getQuestionType():String = prefsUtils.getString(PrefKeys.QUESTION_TYPE, "") ?: ""
}