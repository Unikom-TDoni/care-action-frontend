package edu.rpl.careaction.core.domain

import okhttp3.ResponseBody
import kotlinx.coroutines.Dispatchers
import edu.rpl.careaction.core.helper.GsonMapperHelper

abstract class Repository {
    protected val defaultDispatcher = Dispatchers.IO

    protected fun onApiResponseError(responseBody: ResponseBody) =
        GsonMapperHelper.mapToDto(
            responseBody.charStream(),
            ErrorResponse::class.java
        )
}