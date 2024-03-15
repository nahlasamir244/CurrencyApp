package com.nahlasamir244.core.datautils

sealed interface ResultException

class NoInternetException(message: String? = "Not connected to internet") :
    RuntimeException(message), ResultException

open class ApiException(val code: Int, message: String? = "Code: $code") :
    RuntimeException(message), ResultException

class InternalServerErrorException(
    code: Int,
    message: String? = "service is down !! (code: $code)"
) : ApiException(code, message)

class ResourceNotFoundException(
    code: Int = 404,
    message: String? = "The requested resource does not exist (code: $code)"
) :
    ApiException(code, message)

class InvalidApiKeyException(
    code: Int = 101,
    message: String? = "No API Key was specified or an invalid API Key was specified (code: $code)"
) :
    ApiException(code, message)

class InvalidEndpointException(
    code: Int = 103,
    message: String? = "The requested API endpoint does not exist (code: $code)"
) :
    ApiException(code, message)

class MonthlyRequestLimitExceededException(
    code: Int = 104,
    message: String? = "The maximum allowed API amount of monthly API requests has been reached (code: $code)"
) :
    ApiException(code, message)

class UnsupportedEndpointException(
    code: Int = 105,
    message: String? = "The current subscription plan does not support this API endpoint (code: $code)"
) :
    ApiException(code, message)

class EmptyResultException(
    code: Int = 106,
    message: String? = "The current request did not return any results (code: $code)"
) :
    ApiException(code, message)

class InactiveAccountException(
    code: Int = 102,
    message: String? = "The account this API request is coming from is inactive (code: $code)"
) :
    ApiException(code, message)

class InvalidBaseCurrencyException(
    code: Int = 201,
    message: String? = "An invalid base currency has been entered (code: $code)"
) :
    ApiException(code, message)

class InvalidSymbolException(
    code: Int = 202,
    message: String? = "One or more invalid symbols have been specified (code: $code)"
) :
    ApiException(code, message)

class NoDateSpecifiedException(
    code: Int = 301,
    message: String? = "No date has been specified (code: $code)"
) :
    ApiException(code, message)

class InvalidDateException(
    code: Int = 302,
    message: String? = "An invalid date has been specified (code: $code)"
) :
    ApiException(code, message)

class NoAmountSpecifiedException(
    code: Int = 403,
    message: String? = "No or an invalid amount has been specified (code: $code)"
) :
    ApiException(code, message)

class NoTimeframeSpecifiedException(
    code: Int = 501,
    message: String? = "No or an invalid timeframe has been specified (code: $code)"
) :
    ApiException(code, message)

class InvalidStartDateException(
    code: Int = 502,
    message: String? = "No or an invalid 'start_date' has been specified (code: $code)"
) :
    ApiException(code, message)

class InvalidEndDateException(
    code: Int = 503,
    message: String? = "No or an invalid 'end_date' has been specified (code: $code)"
) :
    ApiException(code, message)

class InvalidTimeframeException(
    code: Int = 504,
    message: String? = "An invalid timeframe has been specified (code: $code)"
) :
    ApiException(code, message)

class ExceededTimeframeException(
    code: Int = 505,
    message: String? = "The specified timeframe is too long, exceeding 365 days (code: $code)"
) :
    ApiException(code, message)