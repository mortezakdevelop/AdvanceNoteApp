package com.example.advancenoteapplication.epoxy

import com.airbnb.epoxy.EpoxyController
import com.example.advancenoteapplication.R
import com.example.advancenoteapplication.addHeaderModel
import com.example.advancenoteapplication.database.entity.CategoryEntity
import com.example.advancenoteapplication.databinding.ModelCategoryBinding
import com.example.advancenoteapplication.databinding.ModelEmptyButtonBinding

class CategoryEpoxyController(
    private val onCategoryEmptyStateClicked: () -> Unit
):EpoxyController() {

    // handle get list state
    var categoryList :List<CategoryEntity> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        addHeaderModel("Categories", com.google.android.material.R.color.mtrl_btn_transparent_bg_color)

        categoryList.forEach {
            CategoryEpoxyModel(it).id(it.id).addTo(this)
        }

        EmptyButtonEpoxyModel("Add Category", onCategoryEmptyStateClicked)
            .id("add_category")
            .addTo(this)

    }

    data class CategoryEpoxyModel(
        val categoryEntity: CategoryEntity,
    ) : ViewBindingKotlinModel<ModelCategoryBinding>(R.layout.model_category) {

        override fun ModelCategoryBinding.bind() {
            textView.text = categoryEntity.name
        }
    }

    data class EmptyButtonEpoxyModel(
        val buttonText: String,
        val onClicked: () -> Unit
    ) : ViewBindingKotlinModel<ModelEmptyButtonBinding>(R.layout.model_empty_button) {

        override fun ModelEmptyButtonBinding.bind() {
            button.text = buttonText
            button.setOnClickListener {
                onClicked.invoke()
            }
        }

        override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
            return totalSpanCount
        }
    }
}