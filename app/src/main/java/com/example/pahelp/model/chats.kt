package com.example.pahelp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.pahelp.db.repository.MessageListConverter


@Entity(tableName = "chats")
@TypeConverters(MessageListConverter::class)
data class Chat(
    @PrimaryKey val date: String,
    val messages: MutableList<Message>
)