package com.example.cookitease.retrofit
import com.example.cookitease.pojo.ChatGPTRequest
import com.example.cookitease.pojo.ChatGPTResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ChatGPTApi {
    @POST("/v1/chat/completions")
    fun chatCompletion(@Body request: ChatGPTRequest): Call<ChatGPTResponse>
}