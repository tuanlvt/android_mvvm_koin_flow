package com.example.flowmvvm.di

import android.app.Application
import com.example.flowmvvm.BuildConfig
import com.example.flowmvvm.data.source.remote.api.middleware.InterceptorImpl
import com.example.flowmvvm.data.source.remote.service.ApiService
import com.example.flowmvvm.data.source.repositories.TokenRepository
import com.example.flowmvvm.di.NetWorkInstant.CONNECTION_TIMEOUT
import com.example.flowmvvm.di.NetWorkInstant.READ_TIMEOUT
import com.example.flowmvvm.di.NetWorkInstant.WRITE_TIMEOUT
import com.google.gson.Gson
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val NetworkModule = module {
    
    single { provideOkHttpCache(get()) }
    
    single { provideOkHttpClient(get(), get()) }
    
    single { provideInterceptor(get()) }
    
    single { provideRetrofit(get(), get()) }
    
    single { provideApiService(get()) }
    
}

fun provideOkHttpCache(app: Application): Cache {
    val cacheSize: Long = 10 * 1024 * 1024 // 10 MiB
    return Cache(app.cacheDir, cacheSize)
}


fun provideInterceptor(tokenRepository: TokenRepository): Interceptor {
    return InterceptorImpl(tokenRepository)
}

fun provideOkHttpClient(cache: Cache, interceptor: Interceptor): OkHttpClient {
    val httpClientBuilder = OkHttpClient.Builder()
    httpClientBuilder.cache(cache)
    httpClientBuilder.addInterceptor(interceptor)
    
    httpClientBuilder.readTimeout(
            READ_TIMEOUT, TimeUnit.SECONDS
    )
    httpClientBuilder.writeTimeout(
            WRITE_TIMEOUT, TimeUnit.SECONDS
    )
    httpClientBuilder.connectTimeout(
            CONNECTION_TIMEOUT, TimeUnit.SECONDS
    )
    
    if (BuildConfig.DEBUG) {
        val logging = HttpLoggingInterceptor()
        httpClientBuilder.addInterceptor(logging)
        logging.level = HttpLoggingInterceptor.Level.BODY
    }
    
    return httpClientBuilder.build()
}

fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
}

fun provideApiService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
}

object NetWorkInstant {
    internal const val READ_TIMEOUT: Long = 30
    internal const val WRITE_TIMEOUT: Long = 30
    internal const val CONNECTION_TIMEOUT: Long = 30
}