package com.juniverse.wheelchat.base

import com.google.firebase.FirebaseException

open class BaseAppException(private val exception: Throwable?) : Exception() {

    constructor(message: String) : this(Throwable(message))

    fun getErrorCode(): Int? =
        if (exception is FirebaseException) exception.hashCode() else null
}