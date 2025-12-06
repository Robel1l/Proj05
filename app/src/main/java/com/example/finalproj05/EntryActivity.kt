package com.example.finalproj05


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

// key used to pass data between activities
const val ENTRY_EXTRA = "ENTRY_EXTRA"

class EntryActivity : AppCompatActivity() {
    // Declaring UI elements and button
    private lateinit var add_food: EditText
    private lateinit var add_cals: EditText
    private lateinit var sB: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)

        // connect UI elements to their xml
        add_food = findViewById(R.id.eats_E)
        add_cals = findViewById(R.id.add_calories)
        sB = findViewById(R.id.subBn)

        // listen when the button is clicked
        sB.setOnClickListener {
            // get what the user inputted
            val added_food_name = add_food.text.toString()
            val number_of_cals = add_cals.text.toString()

            // button works if both input fields are filled
            if (added_food_name.isNotEmpty() && number_of_cals.isNotEmpty()) {
                // convert calories to long and create a new DisplayFood object
                val calories = number_of_cals.toLongOrNull() ?: 0L
                val newFood = DisplayFood(added_food_name, calories)

                // Pass the new food back to MainActivity
                val intent = Intent(this@EntryActivity, MainActivity::class.java)
                // Put the new food in the intent
                intent.putExtra(ENTRY_EXTRA, newFood)

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
                finish()

            } else {
                // Log a warning if both fields are not filled
                Log.w("DB_DEBUG", "Missing name or calories — not inserted.")
            }
        }
    }
}