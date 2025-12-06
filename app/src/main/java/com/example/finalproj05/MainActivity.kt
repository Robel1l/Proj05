package com.example.finalproj05

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    // List of DisplayFoods to display
    private val displayFoods = mutableListOf<DisplayFood>()

    // RecyclerView Ui elements
    private lateinit var foodRe_View: RecyclerView

    // Button Ui elements
    private lateinit var addFood_Bn: Button

    // Adapter for RecyclerView
    private lateinit var food_Adapter: FitAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "launching")

        setContentView(R.layout.activity_main)

        // initialize UI elements
        foodRe_View = findViewById(R.id.foods)
        addFood_Bn = findViewById(R.id.button)

        // Setup RecyclerView and Adapter
        food_Adapter =
            FitAdapter(this, displayFoods) { clickedItem -> onItemClicked(clickedItem) }

        foodRe_View.adapter = food_Adapter
        foodRe_View.layoutManager = LinearLayoutManager(this).also {
            val dividerItemDecoration = DividerItemDecoration(this, it.orientation)
            foodRe_View.addItemDecoration(dividerItemDecoration)
        }


        // observe any changes happening to the db and update the recycler view
        lifecycleScope.launch {
            (application as FitApplication).db.foodDao().getAllFoods().collect { databaseList ->
                Log.d("DB_DEBUG", "Loaded ${databaseList.size} items from DB")
                // Map the database entities to DisplayFood objects
                databaseList.map { entity ->
                    DisplayFood(entity.name, entity.calories)
                }.also { mappedList ->
                    // Update the RecyclerView data
                    displayFoods.clear()
                    displayFoods.addAll(mappedList)
                    food_Adapter.notifyDataSetChanged()
                    Log.d("DB_DEBUG", "Recycler updated with ${mappedList.size} items")
                }
            }
        }

        // checks if new food was added
        handleNewFood(intent)

        // listen when a button is clicked
        addFood_Bn.setOnClickListener {
            Log.d("MainActivity", "add new food clicked")
            val intent = Intent(this, EntryActivity::class.java)
            startActivity(intent)
        }
    }



    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleNewFood(intent)
    }

    // adds new food into database if it exists
    private fun handleNewFood(intent: Intent) {
        val eats = intent.getSerializableExtra(ENTRY_EXTRA) as? DisplayFood
        if (eats != null) {
            Log.d("MainActivity", "Inserting new food: $eats")
            lifecycleScope.launch(IO) {
                (application as FitApplication).db.foodDao().addfood(
                    FoodEntity(
                        name = eats.food_name,
                        calories = eats.calories_num
                    )
                )
            }
        } else {
            Log.d("MainActivity", "No new entry found in intent")
        }
    }

    // deletes food from database when clicked
    private fun onItemClicked(food: DisplayFood) {
        lifecycleScope.launch(IO) {

            val dao = (application as FitApplication).db.foodDao()

            val rows = dao.getAllonce()

            val matching_Food = rows.firstOrNull {
                it.name == food.food_name && it.calories == food.calories_num
            }

            // delete the food from the database
            if (matching_Food != null) {
                dao.deletefood(matching_Food)
                Log.d("MainActivity", "Deleted food: $food")
            }
        }
    }
}
