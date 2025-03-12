package com.example.pahelp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pahelp.model.UserSettings

@Dao
interface UserSettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userSettings: UserSettings)

    @Query("SELECT * FROM user_settings LIMIT 1")
    suspend fun getUser(): UserSettings?
}