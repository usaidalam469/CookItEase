package com.example.cookitease.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cookitease.databinding.PopularRecipesBinding
import com.example.cookitease.pojo.DetailedRecipeList
import com.example.cookitease.pojo.Recipe

class MostPopularRecipeAdapter():RecyclerView.Adapter<MostPopularRecipeAdapter.PopularRecipeViewHolder>() {

    lateinit var onRecipeClick:((Recipe) -> Unit)
    private var recipeList = ArrayList<Recipe>()
    fun setRecipes(recipeList:ArrayList<Recipe>)
    {
        this.recipeList = recipeList
        notifyDataSetChanged()
    }
    fun getRecipes(): ArrayList<Recipe>
    {
        return recipeList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularRecipeViewHolder {
        return PopularRecipeViewHolder(PopularRecipesBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun onBindViewHolder(holder: PopularRecipeViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(recipeList[position].image)
            .into(holder.binding.ivPopularRecipeImage)
        holder.itemView.setOnClickListener{
            onRecipeClick.invoke(recipeList[position])
        }
    }

    class PopularRecipeViewHolder(val binding:PopularRecipesBinding):RecyclerView.ViewHolder(binding.root)
}