package com.example.pahelp.db.repository

import androidx.room.TypeConverter
import com.example.pahelp.model.Message
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MessageListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromMessageList(messages: List<Message>): String {
        return gson.toJson(messages)
    }

    @TypeConverter
    fun toMessageList(data: String): List<Message> {
        val listType = object : TypeToken<List<Message>>() {}.type
        return gson.fromJson(data, listType)
    }
}