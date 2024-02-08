package com.example.cookitease.pojo

data class ChatGPTRequest(
    val messages: List<Message>,
    val model: String
)