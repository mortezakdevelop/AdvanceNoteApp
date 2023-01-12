package com.example.advancenoteapplication.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_entity")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var name:String =""
)


