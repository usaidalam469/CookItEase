package com.example.cookitease.pojo

import java.io.Serializable

data class Result(
    val id: Int,
    val image: String,
    val imageType: String,
    val title: String
): Serializable