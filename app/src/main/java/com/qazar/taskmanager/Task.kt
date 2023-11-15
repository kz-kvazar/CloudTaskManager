package com.qazar.taskmanager
import android.os.Parcel
import android.os.Parcelable

data class Task(
    var time: Long? = null,
    var title: String? = null,
    var description: String? = null,
    var isDone: Boolean? = null,
    var key: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(time ?: 0)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeValue(isDone)
        parcel.writeString(key)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }
}
