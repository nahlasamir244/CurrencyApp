package com.nahlasamir244.data.network.model.response

import com.google.gson.annotations.SerializedName

data class Info(

    @SerializedName("timestamp") var timestamp: Int? = null,
    @SerializedName("rate") var rate: Double? = null
)
