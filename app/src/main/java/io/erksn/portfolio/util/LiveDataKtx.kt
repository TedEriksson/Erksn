package io.erksn.portfolio.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun <T: Any> T.asLiveData(): LiveData<T> {
    return MutableLiveData<T>().apply {
        value = this@asLiveData
    }
}