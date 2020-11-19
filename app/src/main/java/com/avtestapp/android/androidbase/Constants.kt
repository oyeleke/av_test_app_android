
package com.avtestapp.android.androidbase

class PrefKeys {
    companion object {
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val USER_PROFILE = "user_profile"
        const val GENERIC_AUTH_RESPONSE = "auth_response"
        const val USER_RESPONSE = "user_response"
        const val PROFESSIONS_LIST = "profession_list"
        const val HAS_CLICKED_ON_PRACTICE_QUESTIONS = "has_clicked_on_practice question_before"
        const val HAS_CLICKED_ON_STUDY_QUESTIONS = "has_clicked_on_study question_before"

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
