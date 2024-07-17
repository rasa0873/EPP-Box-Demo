package com.example.eppdraft1.main.models

import android.os.Parcel
import android.os.Parcelable

data class Workout(
    var timeCreatedMillis : String = "",
    var description : String = "",
    var dateYear: String = "",
    var dateMonth: String = "",
    var dateDay: String = "",
    var dateTime: String = "",
    var trainerName : String = "",
    var active : String = "Yes",
    var athleteAttendees : ArrayList<Attendee> = ArrayList(),
    var capacity : Int = 8,
    var details : String = "",
    var duration : Int = 1,
    var trainerNotes: String = "",
    var userNotes: ArrayList<UserNotes> = ArrayList()

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createTypedArrayList(Attendee.CREATOR)!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.createTypedArrayList(UserNotes.CREATOR)!!
    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(timeCreatedMillis)
        parcel.writeString(description)
        parcel.writeString(dateYear)
        parcel.writeString(dateMonth)
        parcel.writeString(dateDay)
        parcel.writeString(dateDay)
        parcel.writeString(trainerName)
        parcel.writeString(active)
        parcel.writeTypedList(athleteAttendees)
        parcel.writeInt(capacity)
        parcel.writeString(details)
        parcel.writeInt(duration)
        parcel.writeString(trainerNotes)
        parcel.writeTypedList(userNotes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Workout> {
        override fun createFromParcel(parcel: Parcel): Workout {
            return Workout(parcel)
        }

        override fun newArray(size: Int): Array<Workout?> {
            return arrayOfNulls(size)
        }
    }
}