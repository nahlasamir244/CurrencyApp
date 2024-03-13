package com.nahlasamir244.core.data.network

import com.nahlasamir244.core.data.BuildConfig
import com.nahlasamir244.core.data.utils.NetworkConstants
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class NetworkInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url.newBuilder()
            .addQueryParameter(NetworkConstants.ACCESS_KEY_KEY, BuildConfig.API_KEY).build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}