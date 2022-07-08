package com.example.flowmvvm.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowmvvm.utils.Constants
import com.example.flowmvvm.utils.LogUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    
    var tag: String = ""
    val loadingEvent: MutableStateFlow<Boolean> by lazy { MutableStateFlow(false) }
    val errorEvent: MutableStateFlow<Throwable?> by lazy { MutableStateFlow(null) }
    
    fun setLoading(isShow: Boolean) {
        loadingEvent.value = isShow
    }
    
    fun onError(error: Throwable) {
        errorEvent.value = error
    }
    
    protected fun launch(dispatcher: CoroutineDispatcher = Dispatchers.Main, block: suspend () -> Unit) {
        viewModelScope.launch(dispatcher) {
            block.invoke()
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        LogUtils.d(tag, Constants.RELEASED)
    }
}