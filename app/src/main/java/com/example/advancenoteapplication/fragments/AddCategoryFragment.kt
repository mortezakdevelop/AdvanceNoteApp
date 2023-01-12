package com.example.advancenoteapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.advancenoteapplication.base.BaseFragment
import com.example.advancenoteapplication.database.entity.CategoryEntity
import com.example.advancenoteapplication.databinding.FragmentAddCategoryBinding

class AddCategoryFragment : BaseFragment() {

    private lateinit var fragmentAddCategoryBinding: FragmentAddCategoryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAddCategoryBinding =
            FragmentAddCategoryBinding.inflate(layoutInflater, container, false)
        return fragmentAddCategoryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentAddCategoryBinding.categoryEditText.requestFocus()
        mainActivity.showKeyboard()
        fragmentAddCategoryBinding.saveButton.setOnClickListener {
            saveCategoryToDatabase()
            mainActivity.hideKeyboard(requireView())
        }

        sharedViewModel.completeTransactionLiveData.observe(viewLifecycleOwner) { events ->

            events.getContentIfNotHandledOrReturnNull()?.let {
                findNavController().popBackStack()
            }
        }
    }
    // we use event handling instead of onPause method

//    override fun onPause() {
//        super.onPause()
//        sharedViewModel.completeTransaction.postValue(false)
//    }

    private fun saveCategoryToDatabase() {
        val categoryName = fragmentAddCategoryBinding.categoryEditText.text.toString().trim()
        if (categoryName.isEmpty()) {
            fragmentAddCategoryBinding.categoryTextField.error = "Required field"
            return
        }

        val categoryEntity = CategoryEntity(
            id = 0,
            name = categoryName
        )
        sharedViewModel.insertCategory(categoryEntity)
    }

}