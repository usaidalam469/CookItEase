package com.example.cookitease.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cookitease.databinding.FavoriteRecipeBinding
import com.example.cookitease.databinding.PopularRecipesBinding
import com.example.cookitease.pojo.Recipe
import com.example.cookitease.pojo.Result

class BrowseRecipeAdapter() : RecyclerView.Adapter<BrowseRecipeAdapter.BrowseRecipeViewHolder>() {
    lateinit var onRecipeClick:((Result) -> Unit)
    private var recipeList = ArrayList<Result>()

    class BrowseRecipeViewHolder(val binding: FavoriteRecipeBinding):RecyclerView.ViewHolder(binding.root)

    fun getRecipes():ArrayList<Result>{
        return recipeList
    }
    fun setRecipes(recipeList:ArrayList<Result>)
    {
        this.recipeList = recipeList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrowseRecipeViewHolder {
        return BrowseRecipeViewHolder(FavoriteRecipeBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun onBindViewHolder(holder: BrowseRecipeViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(recipeList[position].image)
            .into(holder.binding.ivFavRecipeImage)
        holder.binding.tvFavTitle.setOnClickListener{
            onRecipeClick.invoke(recipeList[position])
        }
        holder.binding.tvFavTitle.text = recipeList[position].title
        holder.binding.ivFavRecipeDelete.visibility = ImageView.INVISIBLE
    }
}