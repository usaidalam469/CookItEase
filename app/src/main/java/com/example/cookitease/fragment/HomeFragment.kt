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
import com.bumptech.glide.Glide
import com.example.cookitease.activity.RecipeActivity
import com.example.cookitease.adapter.MostPopularRecipeAdapter
import com.example.cookitease.databinding.FragmentHomeBinding
import com.example.cookitease.pojo.Recipe
import com.example.cookitease.pojo.Result
import com.example.cookitease.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var randomRecipe: Recipe
    private lateinit var popularRecipeAdapter: MostPopularRecipeAdapter


    companion object{
        const val RECIPE_ID = "recipeId"
        const val RECIPE_NAME = "recipeName"
        const val RECIPE_IMAGE = "recipeImage"
        const val RECIPE_INSTRUCTION = "recipeInstruction"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        popularRecipeAdapter = MostPopularRecipeAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopularRecipesRecyclerView()

        homeViewModel.getRandomRecipe(1)
        observerRandomRecipe()
        onRandomRecipeClick()

        homeViewModel.getRandomRecipe(10)
        observerPopularRecipe()
        onPopularRecipeClicked()
    }

    private fun onPopularRecipeClicked() {
        try {
            popularRecipeAdapter.onRecipeClick = { recipe: Recipe ->
                val intent = Intent(activity,RecipeActivity::class.java)
                intent.putExtra(RECIPE_ID,recipe.id.toString())
                intent.putExtra(RECIPE_NAME,recipe.title)
                intent.putExtra(RECIPE_IMAGE,recipe.image)
                startActivity(intent)
            }
        }
        catch (e: Exception)
        {

        }

    }

    private fun preparePopularRecipesRecyclerView() {
        binding.rvPopularRecipes.apply {
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
            adapter = popularRecipeAdapter
        }
    }

    private fun onRandomRecipeClick() {
        try {
            binding.cvRandomMeal.setOnClickListener{
                val intent = Intent(activity, RecipeActivity::class.java)
                intent.putExtra(RECIPE_ID,randomRecipe.id.toString())
                intent.putExtra(RECIPE_NAME,randomRecipe.title)
                intent.putExtra(RECIPE_IMAGE,randomRecipe.image)
                startActivity(intent)
            }
        }
        catch (e: Exception)
        {
            Log.e("onRandomRecipeClick", e.message!!)
        }
    }

    private fun observerRandomRecipe() {
        homeViewModel.observeRandomRecipeLiveData().observe(viewLifecycleOwner
        ) { recipe ->
            Glide.with(this@HomeFragment)
                .load(recipe.image)
                .into(binding.ivRandomImage)
            this@HomeFragment.randomRecipe = recipe
        }
    }
    private fun observerPopularRecipe() {
        homeViewModel.observePopularRecipeLiveData().observe(viewLifecycleOwner
        ) { recipeList ->
            popularRecipeAdapter.setRecipes(recipeList = recipeList.recipes as ArrayList<Recipe>)
        }
    }

}