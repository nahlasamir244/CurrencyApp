package com.nahlasamir244.core.data.common

import retrofit2.Response

open class CommonRemoteDataSource {
    @Throws(
        ApiException::class,
        InternalServerErrorException::class, ResourceNotFoundException::class,
        InvalidApiKeyException::class, InvalidEndpointException::class,
        MonthlyRequestLimitExceededException::class, UnsupportedEndpointException::class,
        EmptyResultException::class, InactiveAccountException::class,
        InvalidBaseCurrencyException::class, InvalidSymbolException::class,
        NoDateSpecifiedException::class, InvalidDateException::class,
        NoAmountSpecifiedException::class, NoTimeframeSpecifiedException::class,
        InvalidStartDateException::class, InvalidEndDateException::class,
        InvalidTimeframeException::class, ExceededTimeframeException::class,
    )
    suspend fun <T : BaseResponse> makeRequest(call: suspend () -> Response<T>): T {
        return safeApiResult(call)
    }


    @Throws(
        ApiException::class,
        InternalServerErrorException::class, ResourceNotFoundException::class,
        InvalidApiKeyException::class, InvalidEndpointException::class,
        MonthlyRequestLimitExceededException::class, UnsupportedEndpointException::class,
        EmptyResultException::class, InactiveAccountException::class,
        InvalidBaseCurrencyException::class, InvalidSymbolException::class,
        NoDateSpecifiedException::class, InvalidDateException::class,
        NoAmountSpecifiedException::class, NoTimeframeSpecifiedException::class,
        InvalidStartDateException::class, InvalidEndDateException::class,
        InvalidTimeframeException::class, ExceededTimeframeException::class,
    )
    private suspend fun <T : BaseResponse> safeApiResult(call: suspend () -> Response<T>): T {
        val result = call()
        val responseCode = result.code()
        return when (result.isSuccessful) {
            false -> {
                when {
                    responseCode >= 500 -> throw InternalServerErrorException(code = responseCode)
                    else -> throw ApiException(
                        responseCode,
                        "API response is not successful (code: $responseCode)"
                    )
                }
            }

            true -> {
                when (responseCode) {
                    200 -> {
                        if (result.body()?.success == true) {
                            result.body()!!
                        } else {
                            when (val errorCode = result.body()?.error?.code) {
                                404 -> throw ResourceNotFoundException()
                                101 -> throw InvalidApiKeyException()
                                103 -> throw InvalidEndpointException()
                                104 -> throw MonthlyRequestLimitExceededException()
                                105 -> throw UnsupportedEndpointException()
                                106 -> throw EmptyResultException()
                                102 -> throw InactiveAccountException()
                                201 -> throw InvalidBaseCurrencyException()
                                202 -> throw InvalidSymbolException()
                                301 -> throw NoDateSpecifiedException()
                                302 -> throw InvalidDateException()
                                403 -> throw NoAmountSpecifiedException()
                                501 -> throw NoTimeframeSpecifiedException()
                                502 -> throw InvalidStartDateException()
                                503 -> throw InvalidEndDateException()
                                504 -> throw InvalidTimeframeException()
                                505 -> throw ExceededTimeframeException()
                                else -> throw ApiException(
                                    errorCode ?: 0,
                                    "API response is not successful (code: $errorCode)"
                                )
                            }
                        }
                    }

                    else -> {
                        throw ApiException(
                            responseCode,
                            "API response is not successful (code: $responseCode)"
                        )
                    }
                }
            }
        }
    }
}