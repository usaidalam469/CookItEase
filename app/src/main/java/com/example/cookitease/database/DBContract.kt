package com.example.cookitease.database
import android.provider.BaseColumns

object DBContract {
    class FavoriteRecipe : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorites"
            const val COLUMN_RECIPE_ID = "recipeId"
            const val COLUMN_NAME = "name"
            const val COLUMN_IMAGE = "image"
        }
    }
}