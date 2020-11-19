package com.avtestapp.android.androidbase.av_test.models

enum class QuestionType {
PILOT, FLIGHT_ATTENDANT, AIR_TRAFFIC_CONTROLLER, AIR_DISPATCHER, ENGINEER
}

data class QuestionTypeWithHash(val questionType: QuestionType, val hashString: String)