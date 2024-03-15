package com.nahlasamir244.core.data.common

import android.util.Log
import com.nahlasamir244.core.data.utils.NetworkConnectivityHelper
import com.nahlasamir244.core.datautils.NoInternetException
import com.nahlasamir244.core.datautils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

open class CommonRepo(
    private val networkConnectivityHelper: NetworkConnectivityHelper,
    val ioDispatcher: CoroutineDispatcher
) {

    fun <T> networkFlow(remoteCall: suspend () -> T): Flow<Result<T>> {
        suspend fun fetchFromNetwork(): T {
            if (networkConnectivityHelper.isConnected()) return remoteCall()
            else throw NoInternetException()
        }

        return flow<Result<T>> {
            emit(Result.Loading)
            try {
                emit(Result.Success(fetchFromNetwork()))
            } catch (e: Exception) {
                handleApiException(e, this)
            }
        }.flowOn(ioDispatcher)
    }

    private suspend fun <T> handleApiException(
        e: Exception,
        flowCollector: FlowCollector<Result<T>>
    ) {
        Log.d(TAG, "Exception: ", e)
        flowCollector.emit(Result.Error(e))
    }

    companion object {
        private val TAG = CommonRepo::class.simpleName
    }
}