package com.example.advancenoteapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.advancenoteapplication.database.AppDatabase
import com.example.advancenoteapplication.database.entity.CategoryEntity
import com.example.advancenoteapplication.database.entity.ItemEntity
import com.example.advancenoteapplication.database.entity.ItemWithCategoryEntity
import com.example.advancenoteapplication.events.HandlingEvents
import com.example.advancenoteapplication.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel() : ViewModel() {

    private lateinit var noteRepository: NoteRepository

    //items data
    private val data: MutableLiveData<List<ItemEntity>> = MutableLiveData()
    val liveData: LiveData<List<ItemEntity>> = data

    //category data
    private val categoryData: MutableLiveData<List<CategoryEntity>> = MutableLiveData()
    val categoryLiveData: LiveData<List<CategoryEntity>> = categoryData

    //category data
    private val itemWithCategoryData: MutableLiveData<List<ItemWithCategoryEntity>> =
        MutableLiveData()
    val itemWithCategoryLiveData: LiveData<List<ItemWithCategoryEntity>> = itemWithCategoryData

    val completeTransaction: MutableLiveData<HandlingEvents<Boolean>> = MutableLiveData()
    val completeTransactionLiveData: LiveData<HandlingEvents<Boolean>> = completeTransaction

//    private val _categoryViewStateLiveData = MutableLiveData<CategoriesViewState>()
//    val categoriesViewStateLiveData:LiveData<CategoriesViewState>
//    get() = _categoryViewStateLiveData

    fun init(appDatabase: AppDatabase) {
        noteRepository = NoteRepository(appDatabase)

        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.getAllItems().collect() { items ->
                data.postValue(items)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.getAllItemsWithCategoryEntity().collect() { items ->
                itemWithCategoryData.postValue(items)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.getAllCategory().collect() { items ->
                categoryData.postValue(items)
            }
        }
    }

    //crud for itemsEntity
    fun insertItem(itemEntity: ItemEntity) {
        viewModelScope.launch {
            noteRepository.insertItem(itemEntity)
            completeTransaction.postValue(HandlingEvents(true))
        }
    }

    fun deleteItem(itemEntity: ItemWithCategoryEntity) {
        viewModelScope.launch {
            noteRepository.deleteItem(itemEntity.itemEntity)
        }
    }

    fun updateItem(itemEntity: ItemEntity) {
        viewModelScope.launch {
            noteRepository.updateItem(itemEntity)
        }
    }

    //crud for categoryEntity
    fun insertCategory(categoryEntity: CategoryEntity) {
        viewModelScope.launch {
            noteRepository.insertCategory(categoryEntity)
            completeTransaction.postValue(HandlingEvents(true))
        }
    }

    fun deleteCategory(categoryEntity: CategoryEntity) {
        viewModelScope.launch {
            noteRepository.deleteCategory(categoryEntity)
        }
    }

    fun updateCategory(categoryEntity: CategoryEntity) {
        viewModelScope.launch {
            noteRepository.updateCategory(categoryEntity)
            completeTransaction.postValue(HandlingEvents(true))
        }
    }
//    fun onCategorySelected(categoryId:Int){
//        val categoryViewState = CategoriesViewState(isLoading = true)
//        _categoryViewStateLiveData.postValue(categoryViewState)
//
//        val categories = categoryLiveData.value?:return
//        val viewStateItemList = ArrayList<CategoriesViewState.Item>()
//        categories.forEach{
//            viewStateItemList.add(CategoriesViewState.Item(
//                categoryEntity = it,
//                isSelected = it.id == categoryId
//            ))
//        }

//        val viewState = CategoriesViewState(listItem = viewStateItemList)
//        _categoryViewStateLiveData.postValue(viewState)
    }

////loading state for category section
//data class CategoriesViewState(
//    val isLoading:Boolean = false,
//    val listItem:List<Item> = emptyList()
//){
//    data class Item(
//        val categoryEntity: CategoryEntity = CategoryEntity(0,),
//        val isSelected:Boolean = false
//    )
//}




