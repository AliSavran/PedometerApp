package com.alisavran.pedometerapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getLatestUser(): User?

    @Query("SELECT weight FROM users LIMIT 1")
    suspend fun getUserWeight(): Double?

    @Query("SELECT tall FROM users LIMIT 1")
    suspend fun getUserHeight(): Double?

    @Query("SELECT age FROM users LIMIT 1")
    suspend fun getUserAge(): Int?

    @Query("SELECT gender FROM users LIMIT 1")
    suspend fun getUserGender(): String?
}