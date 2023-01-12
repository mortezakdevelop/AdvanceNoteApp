package com.example.advancenoteapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.advancenoteapplication.R
import com.example.advancenoteapplication.base.BaseFragment
import com.example.advancenoteapplication.databinding.FragmentCategoryBinding
import com.example.advancenoteapplication.epoxy.CategoryEpoxyController

class CategoryFragment : BaseFragment() {
    private lateinit var fragmentCategoryBinding: FragmentCategoryBinding

    private val categoryEpoxyController = CategoryEpoxyController(
        onCategoryEmptyStateClicked = ::onCategoryEmptyStateClicked
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentCategoryBinding = FragmentCategoryBinding.inflate(layoutInflater,container,false)
        fragmentCategoryBinding.epoxyRecyclerView.setController(categoryEpoxyController)
        return fragmentCategoryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.categoryLiveData.observe(viewLifecycleOwner){ categoryEntityList ->
            categoryEpoxyController.categoryList = categoryEntityList
        }
    }

    override fun onResume() {
        super.onResume()
        mainActivity.hideKeyboard(requireView())
    }

    private fun onCategoryEmptyStateClicked(){
        navigateViaNavGraph(R.id.action_categoryFragment_to_addCategoryFragment)
    }
}