package com.nahlasamir244.core.datautils

import com.google.gson.annotations.SerializedName

open class BaseResponse(
    @SerializedName("success") var success: Boolean = false,
    @SerializedName("error") var error: Error? = null,
    @SerializedName("timestamp") var timestamp: Int? = null,
    @SerializedName("date") var date: String? = null,
)

data class Error(
    @SerializedName("code") var code: Int? = null,
    @SerializedName("info") var info: String? = null

)