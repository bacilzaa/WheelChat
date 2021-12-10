package com.juniverse.wheelchat.model

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@IgnoreExtraProperties
data class User(
    val uid:String = "",
    val email:String = "",
    var name:String = "",
    var profile_img:String = "",
): Parcelable{
    @Exclude
    fun toMap():Map<String,Any?>{
        return mapOf(
            "uid" to uid,
            "email" to email,
            "name" to name,
            "profile_img" to profile_img
        )
    }
}
