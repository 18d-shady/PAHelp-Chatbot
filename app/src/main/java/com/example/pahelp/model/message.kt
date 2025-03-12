package com.example.pahelp.model

data class Message(
    val content: String,
    val sending: Boolean,
    val time: String = "",
)