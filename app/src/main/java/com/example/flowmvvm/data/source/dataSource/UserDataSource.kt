package com.example.flowmvvm.data.source.dataSource

import com.example.flowmvvm.data.model.User
import com.example.flowmvvm.data.source.remote.service.ApiService

class UserDataSource
constructor(
    private val query: String,
    private val apiService: ApiService,
) : BaseDataSource<User>() {
    
    override suspend fun loadData(nextPage: Int): List<User> {
        val response = apiService.searchRepository(query, nextPage)
        return response.data ?: emptyList()
    }
    
}