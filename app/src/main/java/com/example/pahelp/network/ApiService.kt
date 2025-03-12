package com.example.pahelp.network


import com.example.pahelp.model.ChatCompletionRequest
import com.example.pahelp.model.ChatCompletionResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Headers

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("chat/completions")
    suspend fun getChatCompletion(@Body request: ChatCompletionRequest): ChatCompletionResponse
}
