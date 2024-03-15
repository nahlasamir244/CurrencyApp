package com.nahlasamir244.core.model.response

import com.google.gson.annotations.SerializedName

data class CurrenciesSymbolsResponse(
    @SerializedName("symbols") var symbols: Map<String, String>? = null

) : BaseResponse()
