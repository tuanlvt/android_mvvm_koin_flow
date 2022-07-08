package com.example.flowmvvm.utils.extension

import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doOnTextChanged
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

fun AppCompatEditText.searchTextChanged(): StateFlow<String> {
    val query = MutableStateFlow("")
    
    doOnTextChanged { text, _, _, _ ->
        query.value = text.toString()
    }
    
    return query
}

fun View.clicks(): Flow<Unit> = callbackFlow {
    setOnClickListener {
        offer(Unit)
    }
    awaitClose { setOnClickListener(null) }
}

fun <T> Flow<T>.throttleFirst(duration: Long): Flow<T> = flow {
    var lastEmissionTime = 0L
    collect { upstream ->
        val currentTime = System.currentTimeMillis()
        val mayEmit = currentTime - lastEmissionTime > duration
        if (mayEmit) {
            lastEmissionTime = currentTime
            emit(upstream)
        }
    }
}

