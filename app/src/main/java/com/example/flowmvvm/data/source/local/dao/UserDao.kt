package com.example.flowmvvm.data.source.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    
    @Query("SELECT * FROM USER_DB")
    fun getAll(): Flow<List<UserEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(user: UserEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateAll(users: List<UserEntity>)
    
    @Delete
    fun delete(user: UserEntity)
    
    @Query("DELETE FROM USER_DB")
    fun deleteAll()
}