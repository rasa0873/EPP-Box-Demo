package com.example.eppdraft1.main.models

import android.os.Parcel
import android.os.Parcelable

data class Attendee(
    var id: String = "",
    var attended: String = ""
    ) : Parcelable  {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(attended)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Attendee> {
        override fun createFromParcel(parcel: Parcel): Attendee {
            return Attendee(parcel)
        }

        override fun newArray(size: Int): Array<Attendee?> {
            return arrayOfNulls(size)
        }
    }
}