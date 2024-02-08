package com.example.cookitease.viewmodel

import android.nfc.Tag
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.cookitease.pojo.DetailedRecipeList
import com.example.cookitease.pojo.Recipe
import com.example.cookitease.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val apiKey="119fc9b4e0a041acbb48d592d00e5e2b"
    private var randomRecipeLiveData=MutableLiveData<Recipe>()
    private var popularRecipeLiveData=MutableLiveData<DetailedRecipeList>()

    fun getRandomRecipe(number:Int)
    {
        RetrofitInstance.api.getRandomRecipe(apiKey,number).enqueue(object: Callback<DetailedRecipeList> {
            override fun onResponse(
                call: Call<DetailedRecipeList>,
                response: Response<DetailedRecipeList>
            ) {
                if(response.body() != null)
                {
                    if(number > 1)
                    {
                        popularRecipeLiveData.value = response.body()!!
                    }
                    else
                    {
                        randomRecipeLiveData.value = response.body()!!.recipes[0]
                    }
                }
                else
                    return
            }

            override fun onFailure(call: Call<DetailedRecipeList>, t: Throwable) {
                Log.e("getRandomRecipe", "Call failed: ${t.message}")
                t.printStackTrace()
            }

        })
    }

    fun observeRandomRecipeLiveData():LiveData<Recipe>{
        return randomRecipeLiveData
    }
    fun observePopularRecipeLiveData():LiveData<DetailedRecipeList>{
        return popularRecipeLiveData
    }
}