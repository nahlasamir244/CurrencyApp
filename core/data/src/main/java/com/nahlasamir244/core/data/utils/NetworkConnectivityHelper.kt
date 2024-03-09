package com.nahlasamir244.core.data.utils

import android.annotation.SuppressLint
import com.blankj.utilcode.util.NetworkUtils
import javax.inject.Inject

open class NetworkConnectivityHelper @Inject constructor() {
    @SuppressLint("MissingPermission")
    open fun isConnected() = NetworkUtils.isConnected()
}