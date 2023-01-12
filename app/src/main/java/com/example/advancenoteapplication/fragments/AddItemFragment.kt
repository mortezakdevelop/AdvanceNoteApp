package com.example.advancenoteapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.advancenoteapplication.R
import com.example.advancenoteapplication.base.BaseFragment
import com.example.advancenoteapplication.database.entity.CategoryEntity
import com.example.advancenoteapplication.database.entity.ItemEntity
import com.example.advancenoteapplication.databinding.FragmentAddItemBinding


class AddItemFragment : BaseFragment() {

    private lateinit var fragmentAddItemBinding: FragmentAddItemBinding
    private val safeArgs:AddItemFragmentArgs by navArgs()
    private val selectedItemEntity :ItemEntity? by lazy {
        sharedViewModel.itemWithCategoryLiveData.value?.find {
            it.itemEntity.id.toString() == safeArgs.selectedItemEntityId
        }?.itemEntity
    }

    private var isInEditMode:Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAddItemBinding = FragmentAddItemBinding.inflate(layoutInflater,container,false)
        return fragmentAddItemBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.completeTransactionLiveData.observe(viewLifecycleOwner){ events ->
            val complete = events.getContentIfNotHandledOrReturnNull()?: false
            if (complete){
                events.let {
                    Toast.makeText(requireContext(),"Item saved!",Toast.LENGTH_LONG).show()
                    fragmentAddItemBinding.titleEditText.text = null
                    fragmentAddItemBinding.titleEditText.requestFocus()

                    fragmentAddItemBinding.descriptionEditText.text = null
                    fragmentAddItemBinding.radioGroup.check(R.id.radioButtonLow)
                }
            }
        }

        //setupScreen in we are in edit mode(update)
        selectedItemEntity?.let { itemEntity ->
            isInEditMode = true
            fragmentAddItemBinding.titleEditText.setText(itemEntity.title)
            fragmentAddItemBinding.descriptionEditText.setText(itemEntity.description)
            when(itemEntity.priority){
                1-> fragmentAddItemBinding.radioGroup.check(R.id.radioButtonLow)
                2-> fragmentAddItemBinding.radioGroup.check(R.id.radioButtonMedium)
                else -> fragmentAddItemBinding.radioGroup.check(R.id.radioButtonHigh)
            }

            fragmentAddItemBinding.saveButton.text = "Update"
            mainActivity.supportActionBar?.title = "Update item"

            //handle progressBar update
            if(itemEntity.title.contains("[")){
                val startIndex = itemEntity.title.indexOf("[") + 1
                val endIndex = itemEntity.title.indexOf("]")
                try {
                    val progress = itemEntity.title.substring(startIndex,endIndex).toInt()
                    fragmentAddItemBinding.quantitySeekBar.progress = progress
                }catch (e:Exception){
                    //handle error
                }
            }

        }

        //handle progressBar to quantity
        fragmentAddItemBinding.quantitySeekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val currentText = fragmentAddItemBinding.titleEditText.text.toString().trim()
                if (currentText.isEmpty()){
                    return
                }
                val startIndex = currentText.indexOf("[") - 1
                val newText = if (startIndex > 0){
                    "${currentText.substring(0 ,startIndex)} [$progress]"
                }else{
                    "$currentText [$progress]"
                }

                val sanitizeText = newText.replace(" [1]","")
                fragmentAddItemBinding.titleEditText.setText(sanitizeText)
                fragmentAddItemBinding.titleEditText.setSelection(sanitizeText.length)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        //save button
        fragmentAddItemBinding.saveButton.setOnClickListener{
            insertDataToDatabase()
            mainActivity.hideKeyboard(requireView())
            if (isInEditMode){
                findNavController().popBackStack()
            }
        }
    }

    // we use event handling instead of onPause method

//    override fun onPause() {
//        super.onPause()
//        sharedViewModel.completeTransaction.postValue(false)
//    }

    private fun insertDataToDatabase(){
        val itemTitle = fragmentAddItemBinding.titleEditText.text.toString().trim()
        val itemDescription = fragmentAddItemBinding.descriptionEditText.text.toString().trim()

        if (itemTitle.isEmpty()){
            fragmentAddItemBinding.titleTextField.error = "Required field"
            return
        }
        fragmentAddItemBinding.titleTextField.error = null

        if(itemDescription.isEmpty()){
            fragmentAddItemBinding.descriptionTextField.error = "Required filed"
            return
        }
        fragmentAddItemBinding.descriptionTextField.error = null

        val itemPriority = when(fragmentAddItemBinding.radioGroup.checkedRadioButtonId){
            R.id.radioButtonLow -> 1
            R.id.radioButtonMedium -> 2
            R.id.radioButtonHigh -> 3
            else -> 0
        }

        if (isInEditMode){
            val itemEntity = selectedItemEntity!!.copy(
                title = itemTitle,
                description = itemDescription,
                priority = itemPriority
            )
            sharedViewModel.updateItem(itemEntity)
            return
        }

        val itemEntity = ItemEntity(
            id = 0,
            description = itemDescription,
            priority = itemPriority,
            title = itemTitle,
            createdAt = System.currentTimeMillis().toInt(),
            categoryId = "" // we will add this part in future
        )

        sharedViewModel.insertItem(itemEntity)
    }
}