package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_records")
data class ScanRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val plantId: String,
    val plantName: String,
    val conditionKey: String,
    val conditionDisplayName: String,
    val confidence: Float,
    val imageUri: String,
    val category: String,
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val isConfident: Boolean = true
)
