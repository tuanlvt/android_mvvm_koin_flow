package com.example.flowmvvm.data.source.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import com.example.flowmvvm.data.model.User
import com.example.flowmvvm.data.source.local.dao.AppDatabase
import com.example.flowmvvm.data.source.local.dao.UserEntity
import com.example.flowmvvm.utils.dispatchers.BaseDispatcherProvider
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

interface AppDBRepository {
    fun getUsers(): LiveData<List<User>>
    
    fun deleteUser(user: User): Flow<Unit>
    
    fun insertUser(user: User): Flow<Unit>
    
    fun insertAllUser(users: List<User>): Flow<Unit>
}

class AppDBRepositoryImpl
constructor(
        private val dispatcherProvider: BaseDispatcherProvider,
        private val appDB: AppDatabase,
        private val gson: Gson) : AppDBRepository {
    
    override fun getUsers(): LiveData<List<User>> {
        return appDB.userDao().getAll()
                .map { list ->
                    list.map { it.userFromEntity(gson) }
                }
                .flowOn(dispatcherProvider.io())
                .asLiveData()
    }
    
    override fun deleteUser(user: User): Flow<Unit> {
        return flow {
            val entity = UserEntity().userToEntity(user, gson)
            appDB.userDao().delete(entity)
            emit(Unit)
        }.flowOn(dispatcherProvider.io())
    }
    
    override fun insertUser(user: User): Flow<Unit> {
        return flow {
            val entity = UserEntity().userToEntity(user, gson)
            appDB.userDao().insertOrUpdate(entity)
            emit(Unit)
        }.flowOn(dispatcherProvider.io())
    }
    
    override fun insertAllUser(users: List<User>): Flow<Unit> {
        return flow {
            val entities = users.map { UserEntity().userToEntity(it, gson) }
            appDB.userDao().insertOrUpdateAll(entities)
            emit(Unit)
        }.flowOn(dispatcherProvider.io())
    }
    
    private fun transformUserEntity(data: LiveData<List<UserEntity>>): LiveData<MutableList<User>> {
        return Transformations.map(data) { result ->
            val list = mutableListOf<User>()
            result?.forEach { entity ->
                list.add(entity.userFromEntity(gson))
            }
            list
        }
    }
}
