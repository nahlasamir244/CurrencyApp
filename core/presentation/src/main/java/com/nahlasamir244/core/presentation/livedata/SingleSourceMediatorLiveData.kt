package com.nahlasamir244.core.presentation.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

class SingleSourceMediatorLiveData<T> : MediatorLiveData<T>() {

    private var lastSource: LiveData<*>? = null

    override fun <S : Any> addSource(source: LiveData<S>, onChanged: Observer<in S>) {
        lastSource?.let { removeSource(it) }
        lastSource = source
        super.addSource(source, onChanged)
    }
}