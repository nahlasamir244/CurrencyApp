package com.nahlasamir244.core.model.response

import com.google.gson.annotations.SerializedName

data class Query(

    @SerializedName("from") var from: String? = null,
    @SerializedName("to") var to: String? = null,
    @SerializedName("amount") var amount: Int? = null

)