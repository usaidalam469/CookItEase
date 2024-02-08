package com.example.cookitease.pojo

data class RecipeList(
    val number: Int,
    val offset: Int,
    val results: List<Result>,
    val totalResults: Int
)