package com.example.advancenoteapplication.base

import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.example.advancenoteapplication.MainActivity
import com.example.advancenoteapplication.R
import com.example.advancenoteapplication.database.AppDatabase
import com.example.advancenoteapplication.viewmodel.NoteViewModel
import com.google.android.material.color.MaterialColors
import com.google.android.material.floatingactionbutton.FloatingActionButton

abstract class BaseFragment:Fragment() {

    protected val mainActivity:MainActivity
    get() = activity as MainActivity

    protected val appDatabase:AppDatabase
    get() = AppDatabase.getDatabase(requireContext())

    // when a fragment destroy, this view Model not destroy(deference between viewModel and activityViewModel)
    protected val sharedViewModel:NoteViewModel by activityViewModels()

    //create custom navigation
    protected fun navigateViaNavGraph(actionId:Int){
        mainActivity.navController.navigate(actionId)
    }

    //create custom navigation
    protected fun navigateViaNavGraph(navDirection:NavDirections){
        mainActivity.navController.navigate(navDirection)
    }

    @ColorInt
    protected fun getAttrColor(attResId:Int):Int =
        MaterialColors.getColor(requireView(),attResId)
}