package com.example.cookitease.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cookitease.database.FavoritesDBHelper
import com.example.cookitease.model.RecipeModel

class FavoriteViewModel : ViewModel(){

    private var favoriteRecipesLiveData= MutableLiveData<ArrayList<RecipeModel>>()
    private lateinit var recipesDBHelper : FavoritesDBHelper

    fun getFavoriteRecipes(context: Context)
    {
        try {
            recipesDBHelper = FavoritesDBHelper(context)
            val recipes = recipesDBHelper.getRecipe()
            favoriteRecipesLiveData.value = recipes
        }
        catch (e: Exception)
        {
            Log.d("getFavoriteRecipes", e.message!!)
            favoriteRecipesLiveData.value = ArrayList()
        }
    }
    fun observeFavoriteRecipeLiveData(): LiveData<ArrayList<RecipeModel>> {
        return favoriteRecipesLiveData
    }
}