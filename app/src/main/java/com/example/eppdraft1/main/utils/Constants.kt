package com.example.eppdraft1.main.utils

import android.app.Activity
import android.net.Uri
import android.webkit.MimeTypeMap

object Constants {
    const val USERS: String = "users"
    const val TAG: String = "ETIQUETA"
    const val USER_DATA: String = "user_data"

    const val WORKOUTS: String = "workouts"

    const val ID: String = "id"
    const val EMAIL: String = "email"
    const val NAME : String = "name"
    const val CITIZEN_ID: String = "citizenId"
    const val MOBILE: String = "mobile"
    const val IMAGE: String = "image"
    const val ROLE: String = "role"

    const val DATE_MILLIS : String = "timeDateInMillis"

    const val TIME_CREATED_MILLIS : String = "timeCreatedMillis"
    const val DESCRIPTION: String = "description"
    const val DATE_YEAR: String = "dateYear"
    const val DATE_MONTH: String = "dateMonth"
    const val DATE_DAY: String = "dateDay"
    const val DATE_TIME: String = "dateTime"
    const val TRAINER_NAME: String = "trainerName"
    const val ACTIVE: String = "active"
    const val ATHLETE_ATTENDEES: String = "athleteAttendees"
    const val CAPACITY: String = "capacity"

    const val MANAGER: String = "Manager"
    const val TRAINER: String = "Trainer"
    const val ATHLETE: String = "Athlete"

    const val LOGGED_EMAIL: String = "email"
    const val PASSWORD: String = "password"
    const val PROFILE_UPDATED: String = "profile_updated"

    const val MY_LIST: String = "myList"

    const val NOTES: String = "notes"
    const val TRAINER_NOTES: String = "trainerNotes"
    const val USER_NOTES: String = "userNotes"

    const val PLANS: String = "plans"


    fun getFileExtension(activity: Activity, uri: Uri?) : String? {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }


}