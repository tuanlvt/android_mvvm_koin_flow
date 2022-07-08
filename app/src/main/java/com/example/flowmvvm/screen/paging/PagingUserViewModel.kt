package com.example.flowmvvm.screen.paging

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.flowmvvm.base.BaseViewModel
import com.example.flowmvvm.base.paging.NetworkState
import com.example.flowmvvm.data.model.User
import com.example.flowmvvm.data.source.repositories.AppDBRepository
import com.example.flowmvvm.data.source.repositories.UserRepository
import com.example.flowmvvm.utils.LogUtils
import com.example.flowmvvm.utils.dispatchers.BaseDispatcherProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

class PagingUserViewModel
constructor(
    private val userRepository: UserRepository,
    private val appDBRepository: AppDBRepository,
    private val dispatcherProvider: BaseDispatcherProvider,
) : BaseViewModel() {
    
    private val _networkState = MutableStateFlow<NetworkState<Any>>(NetworkState.FETCH)
    val networkState: StateFlow<NetworkState<Any>> = _networkState
    
    fun fetchData() = userRepository
        .searchRepositoryPaging("catch", _networkState)
        .cachedIn(viewModelScope)
    
    fun insertUser(user: User) {
        launch(dispatcherProvider.io()) {
            appDBRepository.insertUser(user)
                .collectLatest {
                    LogUtils.d("Insert User Succeeded: ", user.fullName.toString())
                }
        }
    }
    
}