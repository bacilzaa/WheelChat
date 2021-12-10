//package com.juniverse.wheelchat.base
//
//sealed class UseCaseResult<T> {
//    class Success<T>(val result: Unit) : UseCaseResult<T>()
//    class Error<T>(val throwable: Exception) : UseCaseResult<T>()
//    class Busy<T> : UseCaseResult<T>()
//}
//
//inline fun <T> UseCaseResult<T>.fold(onSuccess: (T) -> Unit, onError: (Exception) -> Unit = {}, onBusy: () -> Unit = {} ) {
//    when (this) {
//        is UseCaseResult.Success -> onSuccess.invoke(result)
//        is UseCaseResult.Error -> onError.invoke(throwable)
//        is UseCaseResult.Busy -> onBusy.invoke()
//    }
//}
//
//fun <T> UseCaseResult<T>.getOrNull(): T? = when (this) {
//    is UseCaseResult.Success -> result
//    else -> null
//}
//
//fun <T> UseCaseResult<T>.exceptionOrNull(): Throwable? = when (this) {
//    is UseCaseResult.Error -> throwable
//    else -> null
//}

