package com.example.pahelp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_settings")
data class UserSettings(
    @PrimaryKey val id: Int = 0,
    val wallpaperUri: String? = null
)