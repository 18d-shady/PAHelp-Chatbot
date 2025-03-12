package com.example.pahelp.network

import com.example.pahelp.model.AdminUtil
import io.github.cdimascio.dotenv.Dotenv
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.Interceptor

object RetrofitInstance {

    private val dotenv = Dotenv.load()


    private const val BASE_URL = "https://api.openai.com/v1/"
    private val API_KEY: String = dotenv["OPENAI_API_KEY"] ?: throw IllegalArgumentException("API key not found")

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("Authorization", "Bearer $API_KEY")
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        })
        .build()

    val service: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
