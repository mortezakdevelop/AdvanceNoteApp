package com.example.advancenoteapplication.database.dao

import androidx.room.*
import com.example.advancenoteapplication.database.entity.ItemEntity
import com.example.advancenoteapplication.database.entity.ItemWithCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM item_entity")
    fun getAllItems(): Flow<List<ItemEntity>>

    @Transaction
    @Query("SELECT * FROM item_entity")
    fun getAllItemsWithCategoryEntity(): Flow<List<ItemWithCategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(itemEntity: ItemEntity)

    @Delete
    suspend fun deleteItem(itemEntity: ItemEntity)

    @Update
    suspend fun updateItem(itemEntity: ItemEntity)

}