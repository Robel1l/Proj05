package com.example.finalproj05

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FitAdapter(
    // Context is needed to inflate the layout
    private val context: Context,

    // List of Foods to display in the RecyclerView
    private val foodList: List<DisplayFood>,

    // function to handle click events
    private val onClickedFoods: (food: DisplayFood) -> Unit
) : RecyclerView.Adapter<FitAdapter.ViewHolder>() {

    //creates the ViewHolder by inflating the layout

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false)
        return ViewHolder(view)
    }

    // Bind data and set click listener to the item view.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food_item = foodList[position]
        holder.bind(food_item)

        // Set the click listener on the item view
        holder.itemView.setOnClickListener {
            onClickedFoods(food_item)
        }
    }

    // returns the number of foods in the list
    override fun getItemCount() = foodList.size


    // A ViewHolder within the RecyclerView.
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val food_Name_TV = itemView.findViewById<TextView>(R.id.eats_name)
        private val cals_Tv = itemView.findViewById<TextView>(R.id.cal_num)


        fun bind(food: DisplayFood) {
            food_Name_TV.text = food.food_name
            cals_Tv.text = "${food.calories_num} cal"
        }
    }
}
