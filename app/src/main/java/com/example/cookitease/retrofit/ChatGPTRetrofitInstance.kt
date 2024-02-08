package com.example.cookitease.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ChatGPTRetrofitInstance {
    private val API_KEY="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"
    private const val TIMEOUT_SECONDS = 100L
    private const val BASE_URL = "https://api.openai.com/v1/"

    val api:ChatGPTApi by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(API_KEY))
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChatGPTApi::class.java)
    }
}
class AuthInterceptor(private val apiKey: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $apiKey")
            .build()
        return chain.proceed(newRequest)
    }
}