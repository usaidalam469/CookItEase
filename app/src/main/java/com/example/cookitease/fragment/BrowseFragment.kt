package com.example.cookitease.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookitease.R
import com.example.cookitease.activity.RecipeActivity
import com.example.cookitease.adapter.BrowseRecipeAdapter
import com.example.cookitease.adapter.MostPopularRecipeAdapter
import com.example.cookitease.databinding.FragmentBrowseBinding
import com.example.cookitease.pojo.Recipe
import com.example.cookitease.pojo.Result
import com.example.cookitease.viewmodel.BrowseViewModel
import com.example.cookitease.viewmodel.HomeViewModel

class BrowseFragment : Fragment() {
    private lateinit var binding: FragmentBrowseBinding
    private lateinit var browseViewModel: BrowseViewModel
    private lateinit var browseRecipeAdapter: BrowseRecipeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        browseViewModel = ViewModelProvider(this)[BrowseViewModel::class.java]
        browseRecipeAdapter = BrowseRecipeAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBrowseBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareBrowseRecipesRecyclerView()
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query!=null)
                {
                    browseViewModel.getRecipes(query)
                    observerBrowseRecipe()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
        onRecipeClicked()
    }
    private fun onRecipeClicked() {
        try {
            browseRecipeAdapter.onRecipeClick = { recipe: Result ->
                val intent = Intent(activity, RecipeActivity::class.java)
                intent.putExtra(HomeFragment.RECIPE_ID,recipe.id.toString())
                intent.putExtra(HomeFragment.RECIPE_NAME,recipe.title)
                intent.putExtra(HomeFragment.RECIPE_IMAGE,recipe.image)
                startActivity(intent)
            }
        }
        catch (e: Exception)
        {
            Log.e("onRecipeClicked",e.message!!)
        }
    }

    private fun prepareBrowseRecipesRecyclerView() {
        binding.rvBrowseRecipes.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
            adapter = browseRecipeAdapter
        }
    }

    private fun observerBrowseRecipe() {
        browseViewModel.observeRecipeLiveData().observe(viewLifecycleOwner
        ) { recipeList ->
            browseRecipeAdapter.setRecipes(recipeList = recipeList.results as ArrayList<Result>)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("RecipeList", browseRecipeAdapter.getRecipes())
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val recipeList =savedInstanceState.getSerializable("RecipeList") as ArrayList<Result>
            browseRecipeAdapter.setRecipes(recipeList)
        }
        super.onViewStateRestored(savedInstanceState)
    }
}