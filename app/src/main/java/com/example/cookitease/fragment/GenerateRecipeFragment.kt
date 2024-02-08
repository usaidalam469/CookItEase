package com.example.cookitease.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.cookitease.R
import com.example.cookitease.activity.RecipeActivity
import com.example.cookitease.databinding.FragmentGenerateRecipeBinding

class GenerateRecipeFragment : Fragment() {

    private lateinit var binding: FragmentGenerateRecipeBinding
    private lateinit var ingredientListAdapter: ArrayAdapter<String>
    private lateinit var thingListAdapter: ArrayAdapter<String>
    private var selectedMealType:String = ""
    private val ingredientList = ArrayList<String>()
    private val thingList = ArrayList<String>()
    private val ingredientListKey = "IngredientsList"
    private val thingListKey = "ThingsList"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGenerateRecipeBinding.inflate(inflater, container, false)
        ingredientListAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, ingredientList)
        thingListAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, thingList)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.meals,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.mealType.adapter = adapter
        }
        binding.ingredientListView.adapter = ingredientListAdapter
        binding.thingsListView.adapter = thingListAdapter

        binding.addIngredientButton.setOnClickListener {
            val ingredient = binding.ingredientEditText.text.toString().trim()
            addIngredientToList(ingredient)
            binding.ingredientEditText.text.clear()
        }
        binding.addThingButton.setOnClickListener {
            val thing = binding.thingsEditText.text.toString().trim()
            addThingToList(thing)
            binding.thingsEditText.text.clear()
        }
        binding.ingredientListView.setOnItemLongClickListener { _, _, position, _ ->
            // Handle long click on an item (delete ingredient)
            deleteIngredient(position)
            true
        }
        binding.thingsListView.setOnItemLongClickListener { _, _, position, _ ->
            // Handle long click on an item (delete ingredient)
            deleteThing(position)
            true
        }
        binding.submitButton.setOnClickListener {
            Log.d("ChatGptResponse","Chat gpt call")
            onGenerateRecipeClicked()
        }

        // Set up item selected listener for the Spinner
        binding.mealType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Get the selected meal from the Spinner
                selectedMealType = parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                true
            }
        }
        return binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            var ingredients = savedInstanceState.getStringArrayList(ingredientListKey) ?: ArrayList()
            var things = savedInstanceState.getStringArrayList(thingListKey) ?: ArrayList()
            for(ing in ingredients)
            {
                addIngredientToList(ing)
            }
            for(thing in things)
            {
                addThingToList(thing)
            }
        }
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putStringArrayList(ingredientListKey, ingredientList)
        outState.putStringArrayList(thingListKey, thingList)
        super.onSaveInstanceState(outState)
    }
    private fun addThingToList(thing:String) {
        if (thing.isNotEmpty()) {
            thingList.add(thing)
            thingListAdapter.notifyDataSetChanged()
        }
    }

    private fun deleteIngredient(position: Int) {
        ingredientList.removeAt(position)
        ingredientListAdapter.notifyDataSetChanged()
    }
    private fun deleteThing(position: Int) {
        thingList.removeAt(position)
        thingListAdapter.notifyDataSetChanged()
    }

    private fun addIngredientToList(ingredient:String) {
        if (ingredient.isNotEmpty()) {
            ingredientList.add(ingredient)
            ingredientListAdapter.notifyDataSetChanged()
        }
    }

    private fun onGenerateRecipeClicked()
    {
        if(ingredientList.size == 0)
        {
            Toast.makeText(requireContext(), "No ingredients added!", Toast.LENGTH_SHORT).show()
            return
        }
        val ingredients = ingredientList.joinToString(separator = ", ")
        val things = thingList.joinToString(separator = ", ")
        selectedMealType = selectedMealType ?: "Lunch"
        val message = "Generate a delicious recipe using only the following ingredients: ${ingredients}. Provide clear instructions on how to prepare the dish. \\n Instructions: \\n- Do not use other ingredients \\n- For $selectedMealType \\n Things that I don't have: \\n $things"
        val intent = Intent(activity, RecipeActivity::class.java)
        intent.putExtra(HomeFragment.RECIPE_ID,"0")
        intent.putExtra(HomeFragment.RECIPE_NAME,"AI Generated Recipe")
        intent.putExtra(HomeFragment.RECIPE_IMAGE,"")
        intent.putExtra(HomeFragment.RECIPE_INSTRUCTION,message)
        startActivity(intent)
    }
}