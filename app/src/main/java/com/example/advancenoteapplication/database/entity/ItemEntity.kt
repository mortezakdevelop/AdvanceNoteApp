package com.example.advancenoteapplication.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_entity")
data class ItemEntity(
   @PrimaryKey(autoGenerate = true) val id: Int,
   val title: String,
   val description: String,
   val priority: Int ,
   val createdAt:Int,
   val categoryId: String
)
