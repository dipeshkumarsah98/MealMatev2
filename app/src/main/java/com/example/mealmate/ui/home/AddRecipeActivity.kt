package com.example.mealmate.ui.home

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mealmate.R
import com.example.mealmate.model.Recipe
import com.example.mealmate.viewmodel.RecipeViewModel

class AddRecipeActivity : AppCompatActivity() {
    private val viewModel: RecipeViewModel by viewModels()
    private lateinit var spinnerCategory: Spinner
    private lateinit var categories: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        val etRecipeName = findViewById<EditText>(R.id.etRecipeName)
        val etIngredients = findViewById<EditText>(R.id.etIngredients)
        val etInstructions = findViewById<EditText>(R.id.etInstructions)
        val btnAddRecipe = findViewById<Button>(R.id.btnAddRecipe)
        val etServing = findViewById<EditText>(R.id.etServing)
        spinnerCategory = findViewById(R.id.spinnerCategory)

        // ✅ Define categories for dropdown
        categories = listOf("Breakfast", "Lunch", "Dinner", "Dessert", "Snacks")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter


        // ✅ Show keyboard automatically on Recipe Name input
        showKeyboard(etRecipeName)

        // ✅ Fix cursor anchor issue by restarting input method
        handleFocusFix(etRecipeName)
        handleFocusFix(etIngredients)
        handleFocusFix(etInstructions)

        btnAddRecipe.setOnClickListener {
            val name = etRecipeName.text.toString()
            val ingredients = etIngredients.text.toString().split(",").map { it.trim() }
            val instructions = etInstructions.text.toString()
            val serving = etServing.text.toString()
            val selectedCategory = categories[spinnerCategory.selectedItemPosition]

            if (name.isNotEmpty() && ingredients.isNotEmpty() && instructions.isNotEmpty()) {
                val recipe = Recipe(
                    name = name,
                    ingredients = ingredients,
                    instructions = instructions,
                    serving = serving,
                    category = selectedCategory
                )
                viewModel.addRecipe(recipe)
                finish()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ✅ Show keyboard for the first input field
    private fun showKeyboard(editText: EditText) {
        editText.requestFocus()
        val imm = getSystemService(InputMethodManager::class.java)
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    // ✅ Restart input method on focus to prevent cursor anchor issue
    private fun handleFocusFix(editText: EditText) {
        editText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val imm = getSystemService(InputMethodManager::class.java)
                imm.restartInput(v)
            }
        }
    }
}
