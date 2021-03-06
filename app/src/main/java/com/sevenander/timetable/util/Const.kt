package com.sevenander.timetable.util

/**
 * Created by andrii on 6/1/17.
 */
class Const private constructor() {
    companion object {
        val KEY_LESSON_ID = "lessonId"
        val KEY_LESSON_DAY = "lessonDay"

        val KEY_PREFS = "timetablePrefs"
        val KEY_PREFS_DAYS = "selectedDays"

        val REQUEST_ADD_EDIT = 1000
        val REQUEST_SETTINGS = 2000
    }
}