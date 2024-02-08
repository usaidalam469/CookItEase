package com.example.cookitease.retrofit

import com.example.cookitease.pojo.DetailedRecipeList
import com.example.cookitease.pojo.Recipe
import com.example.cookitease.pojo.RecipeList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpoonacularApi {
    @GET("/recipes/random")
    fun getRandomRecipe(@Query(value= "apiKey") apiKey:String,@Query(value= "number") number:Int): Call<DetailedRecipeList>

    @GET("/recipes/{id}/information")
    fun getRecipeInformation(@Path("id") id:String, @Query(value="apiKey") apiKey: String): Call<Recipe>

    @GET("/recipes/complexSearch")
    fun getRecipes(@Query("query") query:String, @Query(value="apiKey") apiKey: String): Call<RecipeList>
}