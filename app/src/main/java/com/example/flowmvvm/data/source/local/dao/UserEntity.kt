package com.example.flowmvvm.data.source.local.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flowmvvm.data.model.BaseModel
import com.example.flowmvvm.data.model.User
import com.google.gson.Gson

@Entity(tableName = "USER_DB")
data class UserEntity(
        @PrimaryKey var id: Int = BaseModel.UNKNOWN_ID,
        @ColumnInfo(name = "name") var name: String? = "",
        @ColumnInfo(name = "fullName") var fullName: String? = "",
        @ColumnInfo(name = "description") var description: String? = "",
        @ColumnInfo(name = "owner") var owner: String? = "",
        @ColumnInfo(name = "isFavorite") var isFavorite: Boolean = false
) {
    
    fun userToEntity(user: User, gson: Gson): UserEntity {
        this.id = user.id
        this.name = user.name
        this.fullName = user.fullName
        this.owner = JsonConverter.toJson(user.owner, gson)
        this.isFavorite = user.isFavorite
        return this
    }
    
    fun userFromEntity(gson: Gson): User {
        val user = User(
                fullName = fullName,
                description = description,
                owner = JsonConverter.toObject(owner, User.Owner::class.java, gson),
                isFavorite = isFavorite
        )
        user.id = id
        user.name = name
        return user
    }
    
}