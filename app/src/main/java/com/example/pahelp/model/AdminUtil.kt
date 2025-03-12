package com.example.pahelp.model

import io.github.cdimascio.dotenv.Dotenv

data class AdminUtil (
    val adminMessages: AdminMessages = AdminMessages(),
    val API_KEY: String = Dotenv.load()["OPENAI_API_KEY"] ?: throw IllegalArgumentException("API key not found")
)

