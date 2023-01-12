package com.example.advancenoteapplication.epoxy.models

import androidx.core.content.ContextCompat
import com.example.advancenoteapplication.R
import com.example.advancenoteapplication.databinding.ModelHeaderItemBinding
import com.example.advancenoteapplication.epoxy.ViewBindingKotlinModel

// we can input all epoxy models in here
//for sample, we input HeaderEpoxyModel


data class HeaderEpoxyModel(
    private val headerText:String,
    private val headerColor:Int
): ViewBindingKotlinModel<ModelHeaderItemBinding>(R.layout.model_header_item){
    override fun ModelHeaderItemBinding.bind() {
        textView.text = headerText
        textView.setBackgroundColor(headerColor)
        textView.setBackgroundColor(ContextCompat.getColor(root.context,headerColor))

    }

    override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
        return totalSpanCount
    }

}