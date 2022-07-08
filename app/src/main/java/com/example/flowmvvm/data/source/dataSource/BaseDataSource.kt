package com.example.flowmvvm.data.source.dataSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.flowmvvm.base.paging.NetworkState
import com.example.flowmvvm.utils.LogUtils
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * BaseDataSourceFactory
 *
 * @param <T> is Object
 * link: https://developer.android.com/codelabs/android-paging#4
 */

abstract class BaseDataSource<T : Any> : PagingSource<Int, T>() {
    
    abstract suspend fun loadData(nextPage: Int): List<T>
    
    private val mainScope = MainScope()
    private var networkState: MutableStateFlow<NetworkState<*>>? = null
    
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val pageNumber = params.key ?: STARTING_PAGE_INDEX
        
        if (pageNumber == STARTING_PAGE_INDEX) {
            updateState(NetworkState.FETCH)
        } else {
            updateState(NetworkState.LOAD_MORE)
        }
        
        return try {
            val items: List<T> = loadData(nextPage = pageNumber)
            
            updateState(NetworkState.SUCCESS(null))
            
            // Since 0 is the lowest page number, return null to signify no more pages should
            val prevKey = if (pageNumber == STARTING_PAGE_INDEX) null else pageNumber - 1
            
            // data, we return `null` to signify no more pages should be loaded
            val nextKey = if (items.isNotEmpty()) pageNumber + 1 else null
            
            LoadResult.Page(
                data = items,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            updateState(NetworkState.ERROR(exception))
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            updateState(NetworkState.ERROR(exception))
            LoadResult.Error(exception)
        }
    }
    
    // The refresh key is used for subsequent refresh calls to PagingSource.load after the initial load
    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
    
    fun setNetworkState(state: MutableStateFlow<NetworkState<*>>) {
        networkState = state
    }
    
    
    private fun updateState(state: NetworkState<Any>) {
        LogUtils.d("BaseDataSource, NetworkState: ", state.toString())
        mainScope.launch {
            delay(100)
            networkState?.emit(state)
        }
    }
    
    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}
