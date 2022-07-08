package com.example.flowmvvm.base.paging

/*enum class Status {
    FETCH,
    LOAD_MORE,
    SUCCESS,
    FAILED
}

data class NetworkState
constructor(
        val status: Status,
        val error: Throwable? = null
) {
    companion object {
        
        val FETCH = NetworkState(Status.FETCH)
    
        val LOAD_MORE = NetworkState(Status.LOAD_MORE)
        
        val LOADED = NetworkState(Status.SUCCESS)
    
        val FAILED = NetworkState(Status.FAILED)
        
        fun toError(error: Throwable?) = NetworkState(Status.FAILED, error)
    }
}*/

sealed class NetworkState<out T : Any> {
    object FETCH : NetworkState<Nothing>()
    object LOAD_MORE : NetworkState<Nothing>()
    data class SUCCESS<out T : Any>(val data: T?, val isLoadMore: Boolean = false) : NetworkState<T>()
    data class ERROR(val throwable: Throwable) : NetworkState<Nothing>()
}