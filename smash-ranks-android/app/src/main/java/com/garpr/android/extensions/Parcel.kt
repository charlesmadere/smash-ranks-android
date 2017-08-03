package com.garpr.android.extensions

import android.os.Parcel
import android.os.Parcelable

inline fun <reified T : Parcelable> createParcel(crossinline createFromParcel: (Parcel) -> T):
        Parcelable.Creator<T> = object : Parcelable.Creator<T> {
    override fun createFromParcel(source: Parcel?): T = createFromParcel(source)
    override fun newArray(size: Int): Array<out T?> = arrayOfNulls(size)
}

fun Parcel.readInteger(): Int? {
    val value = readString()

    if (value.isNullOrEmpty()) {
        return null
    } else {
        return value.toInt()
    }
}

fun Parcel.writeInteger(integer: Int?) {
    if (integer == null) {
        writeString(null)
    } else {
        writeString(integer.toString())
    }
}
