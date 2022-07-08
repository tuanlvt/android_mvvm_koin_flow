package com.example.flowmvvm.data.source.repositories

import com.example.flowmvvm.data.model.TokenModel
import com.example.flowmvvm.data.source.local.sharedprf.SharedPrefsApi
import com.example.flowmvvm.data.source.local.sharedprf.SharedPrefsKey
import com.example.flowmvvm.utils.extension.notNull

class TokenRepository
constructor(private val sharedPrefsApi: SharedPrefsApi) {
    
    private var tokenModelCache: TokenModel? = null
    
    fun getToken(): TokenModel? {
        tokenModelCache.notNull {
            return it
        }
        
        val token = sharedPrefsApi.get(SharedPrefsKey.KEY_TOKEN, TokenModel::class.java)
        token.notNull {
            tokenModelCache = it
            return it
        }
        
        return null
    }
    
    
    fun saveToken(tokenModel: TokenModel) {
        tokenModelCache = tokenModel
        sharedPrefsApi.put(SharedPrefsKey.KEY_TOKEN, tokenModelCache)
    }
    
    fun clearToken() {
        tokenModelCache = null
        sharedPrefsApi.clear()
    }
    
}