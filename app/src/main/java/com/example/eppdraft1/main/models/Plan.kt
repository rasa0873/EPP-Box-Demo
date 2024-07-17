package com.example.eppdraft1.main.models

import android.os.Parcel
import android.os.Parcelable

data class Plan(
    var planID: String = "",
    var title: String = "",
    var price: Float = 0f,
    var period: String = "",
    var subscribedUsers: ArrayList<User> = ArrayList()
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readFloat(),
        parcel.readString()!!,
        parcel.createTypedArrayList(User.CREATOR)!!
    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(planID)
        parcel.writeString(title)
        parcel.writeFloat(price)
        parcel.writeString(period)
        parcel.writeTypedList(subscribedUsers)
    }


    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<Plan> {
        override fun createFromParcel(parcel: Parcel): Plan {
            return Plan(parcel)
        }

        override fun newArray(size: Int): Array<Plan?> {
            return arrayOfNulls(size)
        }
    }
}