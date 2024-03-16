package com.nahlasamir244.test.util.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.getValue(): T? {
    var response: T? = null
    val observer = Observer<T> { response = it }
    observeForever(observer)
    removeObserver(observer)
    return response
}
