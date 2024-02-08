package com.example.cookitease.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookitease.activity.RecipeActivity
import com.example.cookitease.adapter.FavoriteRecipeAdapter
import com.example.cookitease.database.FavoritesDBHelper
import com.example.cookitease.databinding.FragmentFavoritesBinding
import com.example.cookitease.model.RecipeModel
import com.example.cookitease.viewmodel.FavoriteViewModel

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var favoriteRecipeAdapter: FavoriteRecipeAdapter
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var favoritesDBHelper: FavoritesDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoritesDBHelper =FavoritesDBHelper(requireContext())
        favoriteViewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]
        favoriteRecipeAdapter = FavoriteRecipeAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareFavoriteRecipesRecyclerView()

        favoriteViewModel.getFavoriteRecipes(requireContext())
        observerFavoriteRecipe()
        onFavoriteRecipeClicked()
        onDeleteClicked()
    }
    private fun onDeleteClicked()
    {
        favoriteRecipeAdapter.onDeleteClick = {
            favoritesDBHelper.deleteRecipe(it.recipeId)
            favoriteViewModel.getFavoriteRecipes(requireContext())
        }
    }
    private fun onFavoriteRecipeClicked() {
        try {
            favoriteRecipeAdapter.onRecipeClick = { recipe: RecipeModel ->
                val intent = Intent(activity, RecipeActivity::class.java)
                intent.putExtra(RECIPE_ID,recipe.recipeId.toString())
                intent.putExtra(RECIPE_NAME,recipe.name)
                intent.putExtra(RECIPE_IMAGE,recipe.image)
                startActivity(intent)
            }
        } catch (e: Exception)
        {
            Log.d("onFavoriteRecipeClicked", e.message!!)
        }
    }

    private fun prepareFavoriteRecipesRecyclerView(){
        binding.rvFavoriteRecipes.apply {
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
            adapter = favoriteRecipeAdapter
        }
    }

    private fun observerFavoriteRecipe() {
        favoriteViewModel.observeFavoriteRecipeLiveData().observe(viewLifecycleOwner){
            favoriteRecipeAdapter.setFavoriteRecipes(it)
        }
    }

    companion object {
        const val RECIPE_ID = "recipeId"
        const val RECIPE_NAME = "recipeName"
        const val RECIPE_IMAGE = "recipeImage"
        const val RECIPE_INSTRUCTION = "recipeInstruction"
    }
}