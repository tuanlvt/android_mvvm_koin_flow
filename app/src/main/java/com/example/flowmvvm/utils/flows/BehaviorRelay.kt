package com.example.flowmvvm.utils.flows

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect

/**
 * BehaviorRelay<T>
 *
 * @param <T> is Object
 *
 */

class BehaviorRelay<T>(initValue: T? = null) {
    
    private val _events = MutableSharedFlow<T>() // private mutable shared flow
    val events = _events.asSharedFlow() // publicly exposed as read-only shared flow
    
    private var _value: T? = initValue
    val value: T? get() = _value
    
    suspend fun onNext(value: T) {
        _value = value
        _events.emit(value) // suspends until all subscribers receive it
    }
    
    suspend fun subscribe(action: (value: T) -> Unit) {
        events.collect { action.invoke(it) }
    }
    
}
