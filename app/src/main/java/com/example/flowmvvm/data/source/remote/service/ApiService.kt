package com.example.flowmvvm.data.source.remote.service

import com.example.flowmvvm.data.model.User
import com.example.flowmvvm.data.source.remote.api.response.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * REST API access points
 */
interface ApiService {
    @GET("/search/repositories")
    suspend fun searchRepository(
        @Query("q") query: String?,
        @Query("page") page: Int
    ): ApiResponse<List<User>>
}