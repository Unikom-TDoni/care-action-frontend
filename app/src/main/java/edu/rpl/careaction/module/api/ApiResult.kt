package edu.rpl.careaction.module.api

sealed class ApiResult<out TSuccess, out TError> {
    class Loading<out TSuccess, out TError> : ApiResult<TSuccess, TError>()
    data class Error<out TSuccess, out TError>(val response: TError) : ApiResult<TSuccess, TError>()
    data class Success<out TSuccess, out TError>(val response: TSuccess) : ApiResult<TSuccess, TError>()
}