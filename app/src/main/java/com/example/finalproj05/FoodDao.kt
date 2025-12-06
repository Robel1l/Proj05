package com.example.finalproj05


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    //returns a flow of all foods
    @Query("SELECT * FROM food_table")
    fun getAllFoods(): Flow<List<FoodEntity>>

    // Add a new food to the db
    @Insert
    suspend fun addfood(food: FoodEntity)

    // delete a food from the db
    @Delete
    suspend fun deletefood(food: FoodEntity)

    // Returns all foods
    @Query("SELECT * FROM food_table")
    suspend fun getAllonce(): List<FoodEntity>


}
