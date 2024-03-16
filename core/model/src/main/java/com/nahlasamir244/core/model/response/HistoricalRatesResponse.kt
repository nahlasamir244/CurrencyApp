package com.nahlasamir244.core.model.response

import com.google.gson.annotations.SerializedName
import com.nahlasamir244.core.datautils.BaseResponse

data class HistoricalRatesResponse(
    @SerializedName("historical") var historical: Boolean? = null,
    @SerializedName("base") var base: String? = null,
    @SerializedName("rates") var rates: Map<String, Double>? = null

) : BaseResponse()