package com.example.cookitease.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cookitease.database.FavoritesDBHelper
import com.example.cookitease.model.RecipeModel
import com.example.cookitease.pojo.ChatGPTRequest
import com.example.cookitease.pojo.ChatGPTResponse
import com.example.cookitease.pojo.Recipe
import com.example.cookitease.retrofit.ChatGPTRetrofitInstance
import com.example.cookitease.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeViewModel(): ViewModel() {
    private val apiKey="119fc9b4e0a041acbb48d592d00e5e2b"
    private var recipeLiveData = MutableLiveData<Recipe>()
    private var generateRecipeLiveData= MutableLiveData<ChatGPTResponse>()

    fun getRecipeInformation(id:String)
    {
        RetrofitInstance.api.getRecipeInformation(id,apiKey).enqueue(object: Callback<Recipe> {
            override fun onResponse(
                call: Call<Recipe>,
                response: Response<Recipe>
            ) {
                if(response.body() != null)
                {
                    val randomRecipe: Recipe = response.body()!!
                    recipeLiveData.value = randomRecipe
                }
                else
                    return

            }

            override fun onFailure(call: Call<Recipe>, t: Throwable) {
                Log.d("getRecipeInformation","Failed")
            }

        })
    }

    fun observeRecipeLiveData(): LiveData<Recipe> {
        return recipeLiveData
    }

    fun generateRecipe(gptRequestModel: ChatGPTRequest)
    {
        ChatGPTRetrofitInstance.api.chatCompletion(gptRequestModel).enqueue(object:
            Callback<ChatGPTResponse> {
            override fun onResponse(
                call: Call<ChatGPTResponse>,
                response: Response<ChatGPTResponse>
            ) {
                if (response.body() != null)
                {
                    generateRecipeLiveData.value = response.body()!!
                }
                else
                {
                    Log.d("ChatGptResponse","Error: ${response.code()}")
                    return
                }
            }

            override fun onFailure(call: Call<ChatGPTResponse>, t: Throwable) {
                Log.e("ChatGptResponse", "Call failed: ${t.message}")
                // Optionally, you can also log the stack trace
                t.printStackTrace()
            }

        })
    }
    fun observeGenerateRecipeLiveData(): LiveData<ChatGPTResponse> {
        return generateRecipeLiveData
    }
}