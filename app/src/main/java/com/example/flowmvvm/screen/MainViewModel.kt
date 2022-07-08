package com.example.flowmvvm.screen

import androidx.lifecycle.viewModelScope
import com.example.flowmvvm.base.BaseViewModel
import com.example.flowmvvm.data.model.User
import com.example.flowmvvm.utils.LogUtils
import com.example.flowmvvm.utils.flows.BehaviorRelay
import kotlinx.coroutines.launch

class MainViewModel : BaseViewModel() {
    
    val testBehaviorRelay = BehaviorRelay<User>()
    
    init {
        viewModelScope.launch {
            testBehaviorRelay.subscribe {
                LogUtils.e("testBehaviorRelay", it.fullName.toString())
            }
        }
    }
}

