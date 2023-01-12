package com.example.advancenoteapplication.repository

import com.example.advancenoteapplication.database.AppDatabase
import com.example.advancenoteapplication.database.entity.CategoryEntity
import com.example.advancenoteapplication.database.entity.ItemEntity
import com.example.advancenoteapplication.database.entity.ItemWithCategoryEntity
import kotlinx.coroutines.flow.Flow

class NoteRepository(
    private val appDatabase: AppDatabase
) {

    fun getAllItems(): Flow<List<ItemEntity>> {
        return appDatabase.itemDao().getAllItems()
    }


    fun getAllItemsWithCategoryEntity(): Flow<List<ItemWithCategoryEntity>> {
        return appDatabase.itemDao().getAllItemsWithCategoryEntity()
    }

    suspend fun insertItem(itemEntity: ItemEntity) {
        appDatabase.itemDao().insertItem(itemEntity)
    }

    suspend fun deleteItem(itemEntity: ItemEntity) {
        appDatabase.itemDao().deleteItem(itemEntity)
    }

    suspend fun updateItem(itemEntity: ItemEntity) {
        appDatabase.itemDao().updateItem(itemEntity)
    }


    // logic for category
    fun getAllCategory(): Flow<List<CategoryEntity>> {
        return appDatabase.categoryDao().getAllCategories()
    }

    suspend fun insertCategory(categoryEntity: CategoryEntity) {
        appDatabase.categoryDao().insertCategory(categoryEntity)
    }

    suspend fun deleteCategory(categoryEntity: CategoryEntity) {
        appDatabase.categoryDao().deleteCategory(categoryEntity)
    }

    suspend fun updateCategory(categoryEntity: CategoryEntity) {
        appDatabase.categoryDao().updateCategory(categoryEntity)
    }

}