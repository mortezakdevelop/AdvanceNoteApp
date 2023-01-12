package com.example.advancenoteapplication.events

open class HandlingEvents<out T>(private val content:T) {
    var hasBeenHandled = false
        private set // allow external read but not write

    fun getContentIfNotHandledOrReturnNull():T?{
        return  if (hasBeenHandled){
            null
        }else{
            hasBeenHandled = true
            content
        }
    }

    fun peekContent():T?{
        return content
    }

}