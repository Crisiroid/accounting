package com.crisiroid.accounting.local.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Person(
    @PrimaryKey val nationalId: String,
    val fullName: String,
    val phone: String,
    val username: String,
    val password: String
)
