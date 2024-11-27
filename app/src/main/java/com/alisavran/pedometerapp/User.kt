package com.alisavran.pedometerapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val surname: String,
    val age: Int,
    val tall: Double,
    val weight: Double,
    val gender: String
)