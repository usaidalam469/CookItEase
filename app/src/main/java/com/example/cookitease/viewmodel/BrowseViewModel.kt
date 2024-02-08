package com.example.cookitease.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cookitease.pojo.RecipeList
import com.example.cookitease.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BrowseViewModel: ViewModel() {
    private val apiKey="119fc9b4e0a041acbb48d592d00e5e2b"
    private var recipeResultLiveData = MutableLiveData<RecipeList>()

    fun getRecipes(query:String)
    {
        //Fetching recipes based on the query
        RetrofitInstance.api.getRecipes(query,apiKey).enqueue(object: Callback<RecipeList> {
            // Callback for successful response
            override fun onResponse(
                call: Call<RecipeList>,
                response: Response<RecipeList>
            ) {
                //Assigning response to live data if the response body is not null
                if(response.body() != null)
                    // Update the LiveData with the received RecipeList
                    recipeResultLiveData.value = response.body()!!
                else
                    return
            }
            // Callback for failed response
            override fun onFailure(call: Call<RecipeList>, t: Throwable) {
                // Log an error message if the API call fails
                Log.e("getRecipes",t.message.toString())
            }
        })
    }
    // Function to observe the LiveData containing the recipe results
    fun observeRecipeLiveData(): LiveData<RecipeList> {
        return recipeResultLiveData
    }

}