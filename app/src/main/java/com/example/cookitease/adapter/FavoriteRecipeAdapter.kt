package com.example.cookitease.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cookitease.database.FavoritesDBHelper
import com.example.cookitease.databinding.FavoriteRecipeBinding
import com.example.cookitease.model.RecipeModel

class FavoriteRecipeAdapter() : RecyclerView.Adapter<FavoriteRecipeAdapter.FavoriteRecipeViewHolder>() {
    private var favRecipeList = ArrayList<RecipeModel>()
    lateinit var onRecipeClick:((RecipeModel) -> Unit)
    lateinit var onDeleteClick:((RecipeModel) -> Unit)

    class FavoriteRecipeViewHolder(val binding: FavoriteRecipeBinding):RecyclerView.ViewHolder(binding.root)

    fun setFavoriteRecipes(favRecipeList:ArrayList<RecipeModel>)
    {
        this.favRecipeList = favRecipeList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteRecipeViewHolder {
        return FavoriteRecipeViewHolder(FavoriteRecipeBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return favRecipeList.size
    }

    override fun onBindViewHolder(holder: FavoriteRecipeViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(favRecipeList[position].image)
            .into(holder.binding.ivFavRecipeImage)
        holder.binding.cvRecipe.setOnClickListener{
            onRecipeClick.invoke(favRecipeList[position])
        }
        holder.binding.ivFavRecipeDelete.setOnClickListener{
            onDeleteClick.invoke(favRecipeList[position])
        }
        holder.binding.tvFavTitle.text = favRecipeList[position].name
    }
}