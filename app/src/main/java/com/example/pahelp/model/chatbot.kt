package com.example.pahelp.model


data class ChatMessage(
    val role: String,
    val content: String
)

data class ChatCompletionRequest(
    val model: String,
    val messages: List<ChatMessage>,
    val temperature: Double
)

data class ChatCompletionResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: ChatMessage
)