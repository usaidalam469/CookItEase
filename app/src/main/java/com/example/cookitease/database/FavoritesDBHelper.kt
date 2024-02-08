package com.example.cookitease.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.cookitease.model.RecipeModel

class FavoritesDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    @Throws(SQLiteConstraintException::class)
    fun insertRecipe(recipe: RecipeModel): Boolean
    {
        // Gets the data repository in write mode
        if(getRecipe(recipe.recipeId).size > 0)
        {
            deleteRecipe(recipe.recipeId)
            return false
        }
        val db = writableDatabase
        val values = ContentValues()
        values.put(DBContract.FavoriteRecipe.COLUMN_RECIPE_ID, recipe.recipeId)
        values.put(DBContract.FavoriteRecipe.COLUMN_NAME, recipe.name)
        values.put(DBContract.FavoriteRecipe.COLUMN_IMAGE, recipe.image)
        // Insert the new row, returning the primary key value of the new row
        db.insert(DBContract.FavoriteRecipe.TABLE_NAME, null, values)
        return true
    }
    @Throws(SQLiteConstraintException::class)
    fun deleteRecipe(recipeId: String): Boolean {
        if(getRecipe(recipeId).size == 0)
            return false
        // Gets the data repository in write mode
        val db = writableDatabase
        // Define 'where' part of query.
        val selection = DBContract.FavoriteRecipe.COLUMN_RECIPE_ID + " LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(recipeId)
        // Issue SQL statement.
        db.delete(DBContract.FavoriteRecipe.TABLE_NAME, selection, selectionArgs)

        return true
    }
    fun getRecipe(recipeId: String): ArrayList<RecipeModel> {
        val query="select * from " + DBContract.FavoriteRecipe.TABLE_NAME + " WHERE " + DBContract.FavoriteRecipe.COLUMN_RECIPE_ID + "='" + recipeId + "'"
        return fetchRecipesByQuery(query)
    }
    fun getRecipe(): ArrayList<RecipeModel> {
        val query ="select * from " + DBContract.FavoriteRecipe.TABLE_NAME
        return fetchRecipesByQuery(query)
    }
    fun fetchRecipesByQuery(query:String): ArrayList<RecipeModel>{
        val recipes = ArrayList<RecipeModel>()
        val db = writableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery(query, null)
        } catch (e: SQLiteException)
        {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }
        var recipeId = ""
        var name = ""
        var image = ""
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast) {
                val recipeIdTemp = cursor.getColumnIndex(DBContract.FavoriteRecipe.COLUMN_RECIPE_ID)
                if (recipeIdTemp >= 0) {
                    recipeId = cursor.getString(recipeIdTemp)
                }
                val nameTemp = cursor.getColumnIndex(DBContract.FavoriteRecipe.COLUMN_NAME)
                if (nameTemp >= 0) {
                    name = cursor.getString(nameTemp)
                }
                val imageTemp = cursor.getColumnIndex(DBContract.FavoriteRecipe.COLUMN_IMAGE)
                if (imageTemp>= 0) {
                    image = cursor.getString(imageTemp)
                }
                recipes.add(RecipeModel(recipeId, image,name))
                cursor.moveToNext()
            }
        }
        cursor.close()
        return recipes
    }
    companion object {
        // If you change the database schema, you must increment the database version.
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "CookItEase.db"

        private val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBContract.FavoriteRecipe.TABLE_NAME + " (" +
                    DBContract.FavoriteRecipe.COLUMN_RECIPE_ID + " TEXT PRIMARY KEY," +
                    DBContract.FavoriteRecipe.COLUMN_NAME + " TEXT," +
                    DBContract.FavoriteRecipe.COLUMN_IMAGE + " TEXT)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBContract.FavoriteRecipe.TABLE_NAME
    }
}