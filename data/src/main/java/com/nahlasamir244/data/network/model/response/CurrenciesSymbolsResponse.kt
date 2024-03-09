package com.nahlasamir244.data.network.model.response

import com.google.gson.annotations.SerializedName
import com.nahlasamir244.core.data.common.BaseResponse

data class CurrenciesSymbolsResponse(
    @SerializedName("symbols") var symbols: Map<String, String>? = null

) : BaseResponse()
