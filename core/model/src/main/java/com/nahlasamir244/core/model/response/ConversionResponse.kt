package com.nahlasamir244.core.model.response

import com.google.gson.annotations.SerializedName

data class ConversionResponse(
    @SerializedName("query") var query: Query? = null,
    @SerializedName("info") var info: Info? = null,
    @SerializedName("result") var result: Double? = null

) : BaseResponse()
