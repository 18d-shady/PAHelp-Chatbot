package com.example.pahelp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pahelp.model.Chat

@Dao
interface ChatDao {
    @Insert
    suspend fun insertChat(chat: Chat)

    @Update
    suspend fun updateChat(chat: Chat)

    @Query("SELECT * FROM chats WHERE date = :date")
    suspend fun getChatByDate(date: String): Chat?

    @Query("SELECT * FROM chats ORDER BY date ASC")
    suspend fun getAllChats(): List<Chat>
}