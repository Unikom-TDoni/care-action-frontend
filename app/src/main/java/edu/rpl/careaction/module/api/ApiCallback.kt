package edu.rpl.careaction.module.api

data class ApiCallback<in TSuccess, in TError>(
    val successCallback: (ApiResult.Success<TSuccess, TError>) -> Unit,
    val loadingCallback: (ApiResult.Loading<TSuccess, TError>) -> Unit,
    val errorCallback: (ApiResult.Error<TSuccess, TError>) -> Unit,
)