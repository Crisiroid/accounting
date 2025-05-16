package com.crisiroid.accounting.local.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Receipt(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val date: String,
    val description: String,
    val categoryId: Int
)
