package com.example.flowmvvm.utils.extension

import androidx.lifecycle.MutableLiveData

inline fun <T : Any> T?.notNull(f: (it: T) -> Unit) {
    if (this != null) f(this)
}

inline fun <T : Any> T?.isNull(f: () -> Unit) {
    if (this == null) f()
}

val <T> List<T>.lastIndex: Int
    get() = size - 1


fun MutableList<Any>.swap(index1: Int, index2: Int) {

    val tmp = this[index1]

    this[index1] = this[index2]

    this[index2] = tmp

}

operator fun <T> MutableLiveData<MutableList<T>>.plusAssign(values: List<T>) {
    val value = this.value ?: arrayListOf()
    value.addAll(values)
    this.value = value
}