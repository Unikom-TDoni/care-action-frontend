package edu.rpl.careaction.module.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class FlowApiBuilder<TSuccessEntity : Any, TErrorEntity : Any, TSuccessResponse : Any> {

    private lateinit var coroutineContext: CoroutineContext
    private lateinit var apiCall: suspend () -> Response<TSuccessResponse>

    private lateinit var defaultErrorResponseInstance: TErrorEntity
    private lateinit var nullApiBodyResponseInstance: TErrorEntity
    private lateinit var failedManageApiErrorResponseInstance: TErrorEntity
    private lateinit var failedManageApiSuccessResponseInstance: TErrorEntity

    private lateinit var manageApiErrorResponse: (errorResponse: ResponseBody) -> TErrorEntity?
    private lateinit var manageApiSuccessResponse: (successResponse: TSuccessResponse) -> TSuccessEntity?

    fun setApiCall(apiCall: suspend () -> Response<TSuccessResponse>) =
        apply { this.apiCall = apiCall }

    fun setCoroutineContext(coroutineContext: CoroutineContext) =
        apply { this.coroutineContext = coroutineContext }

    fun setDefaultErrorResponseInstance(defaultErrorResponseInstance: TErrorEntity) =
        apply { this.defaultErrorResponseInstance = defaultErrorResponseInstance }

    fun setNullApiBodyResponseInstance(nullApiBodyResponseInstance: TErrorEntity) =
        apply { this.nullApiBodyResponseInstance = nullApiBodyResponseInstance }

    fun setFailedManageApiErrorResponseInstance(failedManageApiErrorResponseInstance: TErrorEntity) =
        apply { this.failedManageApiErrorResponseInstance = failedManageApiErrorResponseInstance }

    fun setFailedManageApiSuccessResponseInstance(failedManageApiSuccessResponseInstance: TErrorEntity) =
        apply {
            this.failedManageApiSuccessResponseInstance = failedManageApiSuccessResponseInstance
        }

    fun setManageApiErrorResponse(manageApiErrorResponse: (errorResponse: ResponseBody) -> TErrorEntity?) =
        apply { this.manageApiErrorResponse = manageApiErrorResponse }

    fun setManageApiSuccessResponse(manageApiSuccessResponse: (successResponse: TSuccessResponse) -> TSuccessEntity?) =
        apply { this.manageApiSuccessResponse = manageApiSuccessResponse }

    fun build(): Flow<ApiResult<TSuccessEntity, TErrorEntity>> {
        initDefaultErrorResponseInstance()
        return flow {
            emit(ApiResult.Loading<TSuccessEntity, TErrorEntity>())
            val response = apiCall()
            when {
                response.isSuccessful -> {
                    when (val successResponse = response.body()) {
                        null -> emit(ApiResult.Error(nullApiBodyResponseInstance))
                        else ->
                            when (val result = manageApiSuccessResponse(successResponse)) {
                                null -> emit(ApiResult.Error(failedManageApiSuccessResponseInstance))
                                else -> emit(ApiResult.Success(result))
                            }
                    }
                }
                else -> {
                    when (val errorResponse = response.errorBody()) {
                        null -> emit(ApiResult.Error(nullApiBodyResponseInstance))
                        else ->
                            when (val result = manageApiErrorResponse(errorResponse)) {
                                null -> emit(ApiResult.Error(failedManageApiErrorResponseInstance))
                                else -> emit(ApiResult.Error(result))
                            }
                    }
                }
            }
        }.flowOn(coroutineContext)
    }

    private fun initDefaultErrorResponseInstance() {
        when {
            !::nullApiBodyResponseInstance.isInitialized -> nullApiBodyResponseInstance =
                defaultErrorResponseInstance
            !::failedManageApiErrorResponseInstance.isInitialized -> failedManageApiErrorResponseInstance =
                defaultErrorResponseInstance
            !::failedManageApiSuccessResponseInstance.isInitialized -> failedManageApiSuccessResponseInstance =
                defaultErrorResponseInstance
        }
    }
}