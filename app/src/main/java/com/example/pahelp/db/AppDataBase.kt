package com.example.pahelp.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.pahelp.db.dao.ChatDao
import com.example.pahelp.db.dao.UserSettingsDao
import com.example.pahelp.db.repository.MessageListConverter
import com.example.pahelp.model.Chat
import com.example.pahelp.model.UserSettings

@Database(entities = [Chat::class, UserSettings::class], version = 2)
@TypeConverters(MessageListConverter::class) // Register the TypeConverter
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun userSettingsDao(): UserSettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "chat_database"
                )
                    .addMigrations(MIGRATION_1_2) // Add migration here
                    .build()
                INSTANCE = instance
                instance
            }
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create the UserSettings table
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `user_settings` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `wallpaperUri` TEXT)"
                )
            }
        }
    }
}