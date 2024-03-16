package com.nahlasamir244.core.model.response

import com.google.gson.annotations.SerializedName
import com.nahlasamir244.core.datautils.BaseResponse

data class CurrenciesSymbolsResponse(
    @SerializedName("symbols") var symbols: Map<String, String>? = null

) : BaseResponse()
