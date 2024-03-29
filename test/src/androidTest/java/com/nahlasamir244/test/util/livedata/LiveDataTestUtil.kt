package com.nahlasamir244.test.util.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

const val TIMEOUT: Long = 2

object LiveDataTestUtil {

    fun <T> getValue(liveData: LiveData<T>): T? {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = Observer<T> { o ->
            data = o
            latch.countDown()
        }
        liveData.observeForever(observer)
        latch.await(TIMEOUT, TimeUnit.SECONDS)
        liveData.removeObserver(observer)

        return data
    }
}
