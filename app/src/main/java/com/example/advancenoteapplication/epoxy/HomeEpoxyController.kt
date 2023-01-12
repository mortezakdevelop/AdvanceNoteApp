package com.example.advancenoteapplication.epoxy

import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyController
import com.example.advancenoteapplication.R
import com.example.advancenoteapplication.addHeaderModel
import com.example.advancenoteapplication.database.entity.ItemEntity
import com.example.advancenoteapplication.database.entity.ItemWithCategoryEntity
import com.example.advancenoteapplication.databinding.ModelEmptyStateBinding
import com.example.advancenoteapplication.databinding.ModelEntityItemBinding
import com.example.advancenoteapplication.databinding.ModelHeaderItemBinding
import com.example.advancenoteapplication.epoxy.models.HeaderEpoxyModel


class HomeEpoxyController(
    private val itemClickListener: ItemClickListener
) : EpoxyController() {

    // handle loading state
    private var isLoading: Boolean = true
        set(value) {
            field = value
            if (field) {
                requestModelBuild()
            }
        }

    // handle get list state
    var items: List<ItemWithCategoryEntity> = emptyList()
        set(value) {
            field = value
            isLoading = false
            requestModelBuild()
        }

    override fun buildModels() {
        if (isLoading) {
            LoadingEpoxyModel().id("loading-state").addTo(this)
            return
        }
        if (items.isEmpty()) {
            EmptyStateEpoxyModel().id("empty_state").addTo(this)
            return
        }

        var currentPriority:Int = -1
        items.sortedByDescending {
            it.itemEntity.priority
        }.forEach{ item ->
            if(item.itemEntity.priority != currentPriority){
                currentPriority = item.itemEntity.priority
                val text = getHeaderTextForPriority(currentPriority)
                val color = getHeaderColorForPriority(currentPriority)
                addHeaderModel(text,color)
            }
            ItemEntityEpoxyModel(item,itemClickListener).id(item.itemEntity.id).addTo(this)
        }
    }

    private fun getHeaderTextForPriority(priority:Int):String{
        return when(priority){
            1 -> "Low"
            2 -> "Medium"
            else -> "High"
        }
    }

    private fun getHeaderColorForPriority(priority: Int):Int{
        return when(priority){
            1-> android.R.color.holo_green_light
            2-> android.R.color.holo_orange_light
            else -> android.R.color.holo_red_light
        }
    }


    data class ItemEntityEpoxyModel(
        val itemEntity: ItemWithCategoryEntity,
        val itemClickListener: ItemClickListener
    ):ViewBindingKotlinModel<ModelEntityItemBinding>(R.layout.model_entity_item){

        override fun ModelEntityItemBinding.bind() {
            titleTextView.text = itemEntity.itemEntity.title

            if (itemEntity.itemEntity.description == null){
                descriptionTextView.isGone = true
            }else{
                descriptionTextView.isVisible = true
                descriptionTextView.text = itemEntity.itemEntity.description
            }

//            categoryNameTextView.setOnClickListener {
//                itemClickListener.onDeleteClickListener(itemEntity)
//            }

            priorityTextView.setOnClickListener {
                itemClickListener.onPumpClickListener(itemEntity.itemEntity)
            }

            val colorRes = when(itemEntity.itemEntity.priority){
                1 -> android.R.color.holo_green_dark
                2-> android.R.color.holo_orange_dark
                3 -> android.R.color.holo_red_dark
                else -> android.R.color.holo_green_light
            }

            // handle color stork item
            val color = ContextCompat.getColor(root.context,colorRes)
            priorityTextView.setBackgroundColor(color)
            root.setStrokeColor(ColorStateList.valueOf(color))

            priorityTextView.setBackgroundColor(ContextCompat.getColor(root.context,colorRes))

            //handle click items to update
            root.setOnClickListener {
                itemClickListener.onItemSelected(itemEntity.itemEntity)
            }
        }
    }

    class EmptyStateEpoxyModel:ViewBindingKotlinModel<ModelEmptyStateBinding>(R.layout.model_empty_state){
        override fun ModelEmptyStateBinding.bind() {
            // handle empty state
        }
    }

}