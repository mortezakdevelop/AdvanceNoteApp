package com.example.advancenoteapplication.epoxy

import com.example.advancenoteapplication.database.entity.ItemEntity

interface ItemClickListener {
    fun onPumpClickListener(itemEntity: ItemEntity)
    fun onItemSelected(itemEntity: ItemEntity)
}