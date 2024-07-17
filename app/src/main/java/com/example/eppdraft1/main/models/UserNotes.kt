package com.example.eppdraft1.main.models

import android.os.Parcel
import android.os.Parcelable

data class UserNotes(
    var userId: String = "",
    var notes: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(notes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserNotes> {
        override fun createFromParcel(parcel: Parcel): UserNotes {
            return UserNotes(parcel)
        }

        override fun newArray(size: Int): Array<UserNotes?> {
            return arrayOfNulls(size)
        }
    }
}