package com.example.cookitease.pojo

data class Step(
    val equipment: List<Equipment>,
    val ingredients: List<Any>,
    val length: Length,
    val number: Int,
    val step: String
)