package com.example.advancenoteapplication.epoxy

import com.example.advancenoteapplication.R
import com.example.advancenoteapplication.databinding.EpoxyModelLoadingBinding

class LoadingEpoxyModel:ViewBindingKotlinModel<EpoxyModelLoadingBinding>(R.layout.epoxy_model_loading) {
    override fun EpoxyModelLoadingBinding.bind() {
        // handle loading
    }
}