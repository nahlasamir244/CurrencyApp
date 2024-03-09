package com.nahlasamir244.data.network.model.response

import com.google.gson.annotations.SerializedName
import com.nahlasamir244.core.data.common.BaseResponse

data class ConversionResponse(
    @SerializedName("query") var query: Query? = null,
    @SerializedName("info") var info: Info? = null,
    @SerializedName("result") var result: Double? = null

) : BaseResponse()
