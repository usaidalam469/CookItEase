package com.example.cookitease.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.cookitease.fragment.HomeFragment
import com.example.cookitease.R
import com.example.cookitease.database.FavoritesDBHelper
import com.example.cookitease.databinding.ActivityRecipeBinding
import com.example.cookitease.model.RecipeModel
import com.example.cookitease.pojo.ChatGPTRequest
import com.example.cookitease.pojo.Message
import com.example.cookitease.viewmodel.RecipeViewModel

class RecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeBinding
    private lateinit var recipeId:String
    private lateinit var recipeName:String
    private lateinit var recipeImage:String
    private lateinit var recipeInstruction:String
    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var recipesDBHelper : FavoritesDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try{
            recipesDBHelper = FavoritesDBHelper(this)
            binding = ActivityRecipeBinding.inflate(layoutInflater)
            setContentView(binding.root)
            recipeViewModel = ViewModelProvider(this)[RecipeViewModel::class.java]
            loadingCase()
            getRecipeInformationIntent()
            setInformationInView()
            if(recipeId == "0")
            {
                val message1 = Message("You are a chef assistant, only do what is asked","system")
                val message2 = Message( recipeInstruction ,"user")
                val messageList = ArrayList<Message>()
                messageList.add(message1)
                messageList.add(message2)
                val request = ChatGPTRequest(messageList,"gpt-3.5-turbo")
                recipeViewModel.generateRecipe(request)
                observerGenerateRecipe()
            }
            else
            {
                recipeViewModel.getRecipeInformation(recipeId)
                observerRecipeInformation()
            }
            binding.btnFav.setOnClickListener{
                onFavoriteClicked()
            }
            binding.btnBack.setOnClickListener{
                closeActivity()
            }
        }
        catch (e:Exception)
        {
            Log.d("onCreate", e.message!!)
        }

    }

    private fun observerGenerateRecipe(){
        recipeViewModel.observeGenerateRecipeLiveData().observe(this
        ) { value ->
            responseCase()
            binding.tvIngredientsTitle.text = "Enjoy the recipe!"
            binding.tvIngredientsDetails.text = value.choices[0].message.content
            binding.tvInstructionsDetails.visibility = View.INVISIBLE
            binding.tvInstructionsTitle.visibility = View.INVISIBLE
            binding.btnFav.visibility = View.INVISIBLE
        }
    }
    private fun observerRecipeInformation() {
        recipeViewModel.observeRecipeLiveData().observe(this
        ) { recipe ->
            var recipeModel = recipesDBHelper.getRecipe(recipeId)
            if (recipeModel.size > 0) {
                binding.btnFav.setImageResource(R.drawable.baseline_favorite_24)
            }
            var ingredients = ""
            responseCase()
            for (i in recipe.extendedIngredients) {
                ingredients += " -${i.name}"
            }

            var instruction = "Steps\n"
            for (i in recipe.analyzedInstructions[0].steps) {
                instruction += "Step ${i.number}: ${i.step} \n \n"
            }
            binding.tvInstructionsDetails.text = instruction
            binding.tvIngredientsDetails.text = ingredients
            Log.d("onCreate", recipeId)
        }
    }

    private fun setInformationInView() {
        if(recipeImage != "")
        {
            binding.ivRecipeImage.scaleType = ImageView.ScaleType.CENTER_CROP
            Glide.with(applicationContext)
                .load(recipeImage)
                .into(binding.ivRecipeImage)
            binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
        }
        else
        {
            binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.black))
        }
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.title = recipeName
    }

    private fun getRecipeInformationIntent() {
        val intent = intent
        recipeId = intent.getStringExtra(HomeFragment.RECIPE_ID)!!
        recipeName = intent.getStringExtra(HomeFragment.RECIPE_NAME)!!
        recipeImage = intent.getStringExtra(HomeFragment.RECIPE_IMAGE)!!
        if(recipeId == "0")
            recipeInstruction = intent.getStringExtra(HomeFragment.RECIPE_INSTRUCTION)!!
    }

    private fun onFavoriteClicked()
    {
        val isInserted = recipesDBHelper.insertRecipe(RecipeModel(recipeId,recipeImage,recipeName))
        if(isInserted)
        {
            Toast.makeText(this@RecipeActivity, "Recipe added to favorites!", Toast.LENGTH_SHORT).show()
            binding.btnFav.setImageResource(R.drawable.baseline_favorite_24)
        }
        else
        {
            Toast.makeText(this@RecipeActivity, "Recipe removed from favorites!", Toast.LENGTH_SHORT).show()
            binding.btnFav.setImageResource(R.drawable.baseline_favorite_border_24)
        }
    }

    private fun loadingCase()
    {
        binding.linearProgressIndicator.visibility = View.VISIBLE
        binding.btnFav.visibility = View.INVISIBLE
        binding.tvIngredientsDetails.visibility = View.INVISIBLE
        binding.tvInstructionsDetails.visibility = View.INVISIBLE
        binding.tvInstructionsTitle.visibility = View.INVISIBLE
        binding.tvIngredientsTitle.visibility = View.INVISIBLE
    }
    private fun responseCase()
    {
        binding.linearProgressIndicator.visibility = View.INVISIBLE
        binding.btnFav.visibility = View.VISIBLE
        binding.tvIngredientsDetails.visibility = View.VISIBLE
        binding.tvInstructionsDetails.visibility = View.VISIBLE
        binding.tvInstructionsTitle.visibility = View.VISIBLE
        binding.tvIngredientsTitle.visibility = View.VISIBLE
    }
    private fun closeActivity() {
        finish()
    }
}