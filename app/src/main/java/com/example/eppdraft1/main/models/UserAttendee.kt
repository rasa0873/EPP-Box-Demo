package com.example.eppdraft1.main.models

import android.os.Parcel
import android.os.Parcelable

data class UserAttendee(
    var id: String = "",
    var name: String = "",
    var citizenId: String = "",
    var email: String = "",
    var role: String = "",
    var mobile: Long = 0,
    var image: String = "",
    var status: String = "active",
    var attended: String = ""
        ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(citizenId)
        parcel.writeString(email)
        parcel.writeString(role)
        parcel.writeLong(mobile)
        parcel.writeString(image)
        parcel.writeString(status)
        parcel.writeString(attended)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserAttendee> {
        override fun createFromParcel(parcel: Parcel): UserAttendee {
            return UserAttendee(parcel)
        }

        override fun newArray(size: Int): Array<UserAttendee?> {
            return arrayOfNulls(size)
        }
    }
}
