
package com.avtestapp.android.androidbase

class PrefKeys {
    companion object {
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val USER_PROFILE = "user_profile"
        const val USER_PROFILE_SINGLE = "user_profile_single"
        const val GENERIC_AUTH_RESPONSE = "auth_response"
        const val USER_RESPONSE = "user_response"
        const val PROFESSIONS_LIST = "profession_list"
        const val HAS_CLICKED_ON_PRACTICE_QUESTIONS = "has_clicked_on_practice question_before"
        const val HAS_CLICKED_ON_STUDY_QUESTIONS = "has_clicked_on_study question_before"
        const val HAS_LOGGED_IN_BEFORE = "has_logged_in_before"
        const val LAST_USER_EMAIL = "last_user_email"
        const val PRACTICE_SHUFFLE_QUESTIONS = "practice_shuffle_questions"
        const val STUDY_SHUFFLE_QUESTIONS = "study_shuffle_questions"
        const val SHOW_ONLY_ANSWERS_FOR_STUDY_QUESTIONS = "show_only_answers_for_study_questions"
        const val CURRENT_QUESTIONS_SESSIONS = "current_questions_sessions"
        const val CURRENT_STUDY_QUESTIONS_SESSIONS = "current_study_questions_sessions"
        const val CURRENT_REVIEW_QUESTIONS_SESSIONS = "current_review_questions_sessions"
        const val CURRENT_QUESTIONS_SESSIONS_DETAILS = "current_questions_sessions_details"
        const val CURRENT_STUDY_QUESTIONS_SESSIONS_DETAILS = "current_study_questions_sessions_details"
        const val BOOKMARKED_QUESTIONS = "book_marked_questions"
        const val QUESTION_TYPE = "question_type"
        const val STUDY_BOOKMARKED_QUESTIONS = "study_book_marked_questions"
        const val PRACTICE_QUESTION_ANSWERS = "practice_question_answers"

    }
}

interface UI_CONSTANTS{
    companion object{
        const val FLIGHT_ATTENDANT = "Flight attendant"
        const val PILOT = "Pilot"
        const val AIRCRAFT_MAINTENANCE = "Aircraft maintenance"
        const val AIR_DISPATCHER = "Air dispatcher"
        const val AIR_TRAFFIC_CONTROLLER = "Air traffic controller"
        const val ENGINEER = "Engineer"
    }
}

class Utils {
    companion object {
        const val LIMIT = 40
    }
}
class APIDataKeys
