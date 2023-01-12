package com.example.advancenoteapplication

import com.airbnb.epoxy.EpoxyController
import com.example.advancenoteapplication.epoxy.models.HeaderEpoxyModel

fun EpoxyController.addHeaderModel(headerText:String,headerColor:Int){
    HeaderEpoxyModel(headerText,headerColor).id(headerText).addTo(this)
}