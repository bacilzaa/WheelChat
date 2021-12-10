//package com.juniverse.wheelchat.helper
//
//import com.juniverse.wheelchat.base.BaseAppException
//import com.juniverse.wheelchat.base.UseCaseResult
//
//suspend fun <T> handleUseCaseException(apiCall: suspend () -> Unit): UseCaseResult<T> {
//    return try {
//        UseCaseResult.Success(apiCall.invoke())
//    } catch (throwable: Throwable) {
//        UseCaseResult.Error(BaseAppException(throwable))
//    }
//}
