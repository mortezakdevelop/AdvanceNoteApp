package com.example.advancenoteapplication.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyTouchHelper
import com.example.advancenoteapplication.R
import com.example.advancenoteapplication.base.BaseFragment
import com.example.advancenoteapplication.database.entity.ItemEntity
import com.example.advancenoteapplication.databinding.FragmentHomeBinding
import com.example.advancenoteapplication.epoxy.HomeEpoxyController
import com.example.advancenoteapplication.epoxy.ItemClickListener
import com.google.android.material.snackbar.Snackbar


class HomeFragment : BaseFragment(), ItemClickListener {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        //change fab button icon color
        fragmentHomeBinding.fab.imageTintList = ColorStateList.valueOf(Color.rgb(248, 245, 250))
        hideFabButtonScrolling()
        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val controller = HomeEpoxyController(this)
        fragmentHomeBinding.epoxyRecyclerView.setController(controller)


        sharedViewModel.itemWithCategoryLiveData.observe(viewLifecycleOwner) { items ->
            controller.items = items
        }

        fragmentHomeBinding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addItemFragment)
        }

        EpoxyTouchHelper.initSwiping(fragmentHomeBinding.epoxyRecyclerView)
            .leftAndRight()
            .withTarget(HomeEpoxyController.ItemEntityEpoxyModel::class.java)
            .andCallbacks(object :
                EpoxyTouchHelper.SwipeCallbacks<HomeEpoxyController.ItemEntityEpoxyModel>() {
                override fun onSwipeCompleted(
                    model: HomeEpoxyController.ItemEntityEpoxyModel?,
                    itemView: View?,
                    position: Int,
                    direction: Int
                ) {
                    val itemHasDeleted = model?.itemEntity ?: return
                    sharedViewModel.deleteItem(itemHasDeleted)
                    Snackbar.make(
                        requireContext(),
                        requireView(),
                        itemHasDeleted.itemEntity.title + " Deleted!",
                        Snackbar.LENGTH_LONG
                    ).show()

                }
            })
    }

    override fun onStart() {
        super.onStart()
        fragmentHomeBinding.fab.show()
    }

    override fun onResume() {
        super.onResume()
        mainActivity.hideKeyboard(fragmentHomeBinding.root)
    }

    override fun onPumpClickListener(itemEntity: ItemEntity) {
        val currentPriority = itemEntity.priority
        var newPriority = currentPriority + 1
        if (newPriority > 3) {
            newPriority = 1
        }

        val updateItemEntity = itemEntity.copy(priority = newPriority)
        sharedViewModel.updateItem(updateItemEntity)
    }

    override fun onItemSelected(itemEntity: ItemEntity) {
        val navDirection =
            HomeFragmentDirections.actionHomeFragmentToAddItemFragment(itemEntity.id.toString())
        navigateViaNavGraph(navDirection)
    }

    private fun hideFabButtonScrolling() {
        fragmentHomeBinding.epoxyRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 || dy < 0 && fragmentHomeBinding.fab.isShown)
                    fragmentHomeBinding.fab.hide()
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    fragmentHomeBinding.fab.show()
            }
        })
    }
}


