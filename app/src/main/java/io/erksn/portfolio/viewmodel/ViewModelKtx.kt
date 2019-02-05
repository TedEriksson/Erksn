package io.erksn.portfolio.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

fun <T, K> LiveData<T>.switchMap(switchMapFunction: (T) -> LiveData<K>): LiveData<K> {
    return Transformations.switchMap(this, switchMapFunction)
}

fun <T, K> LiveData<T>.map(mapFunction: (T) -> K): LiveData<K> {
    return Transformations.map(this, mapFunction)
}