package com.ecommerce.testapp

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ecommerce.testapp.activities.MainViewModel
import com.ecommerce.testapp.activities.screens.listing.adapter.SimpleItem
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.golfercard.playsafe.ItemListingFragmentViewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import kotlinx.coroutines.launch


class ItemListingFragment : Fragment() , ItemListAdapter.OnProductListener {

    //private lateinit var itemListingFragmentViewModel: ItemListingFragmentViewModel
    private val itemListingFragmentViewModel: ItemListingFragmentViewModel by activityViewModels()
    private lateinit var adapter:ItemListAdapter
    //private lateinit var horizontalAdapter:ItemListAdapter
    private lateinit var recyclerView: RecyclerView
    private  var  product: List<Product> = listOf()

    private lateinit var horizontalRecyclerView: RecyclerView
    private lateinit var root:View

    private lateinit var loader:ProgressBar

    private val productActivityViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        root = inflater.inflate(R.layout.fragment_item_listing, container, false)
        loader = root.findViewById<ProgressBar>(R.id.items_loader)
        loader.visibility = View.VISIBLE
        adapter = ItemListAdapter(emptyList(), false, this)
        //horizontalAdapter= ItemListAdapter(emptyList(), true, this)
        recyclerView= root.findViewById(R.id.itemsRecycleView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter

        horizontalRecyclerView =  root.findViewById(R.id.horizontalRecycleView)
        //horizontalRecyclerView.setHasFixedSize(true)
        //val snapHelper = GravitySnapHelper(Gravity.START)
        //snapHelper.attachToRecyclerView(horizontalRecyclerView)

        //horizontalRecyclerView.itemAnimator = DefaultItemAnimator()
        //horizontalRecyclerView.adapter = horizontalAdapter
        //snapHelper.attachToRecyclerView(horizontalRecyclerView)



        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                itemListingFragmentViewModel.allProductState.collect { it ->
                    when (it) {
                        is ProductsApiState.Loading -> {
                            loader.visibility = View.VISIBLE
                        }

                        is ProductsApiState.Failure -> {
                            loader.visibility = View.GONE
                            Toast.makeText(requireContext(), "No products found...", Toast.LENGTH_LONG)
                                .show()
                            Log.d("main", "onCreate: ${it.msg}")
                        }
                        is ProductsApiState.Empty -> { }

                        is ProductsApiState.Success -> {
                            loader.visibility = View.GONE
                            if (it.data.isNotEmpty()) {
                                product = it.data
                                adapter.setAllItems(product)
                                //horizontalAdapter.setAllItems(itemList)
                                adapter.notifyDataSetChanged()
                                //When data is loaded...
                                setUpHorizontalFastAdapter(product)

                            }
                        }

                        else -> {}
                    }
                }
            }
        }




        itemListingFragmentViewModel.refresh()
        return root
    }

    private fun setUpHorizontalFastAdapter(localProduct:List<Product>)
    {
        val simpleItemsList: ArrayList<SimpleItem> = ArrayList()

        for (eachItem in localProduct) {
            Log.e("eachItem", eachItem.title +" "+eachItem.price)
            val simpleItem = SimpleItem()
            simpleItem.name = eachItem.title
            simpleItem.price = eachItem.price.toString()
            simpleItem.imageString= eachItem.image
            simpleItemsList.add(simpleItem)
        }
        Log.e("simpleItemsList", simpleItemsList.size.toString())

        //create the ItemAdapter holding your Items
        val itemAdapter = ItemAdapter<SimpleItem>()

        //create the managing FastAdapter, by passing in the itemAdapter
        val fastAdapter = FastAdapter.with(itemAdapter)

        //set our adapters to the RecyclerView
        //horizontalRecyclerView.setAdapter(fastAdapter)
        //horizontalRecyclerView.itemAnimator = DefaultItemAnimator()
        horizontalRecyclerView.adapter = fastAdapter



        itemAdapter.add(simpleItemsList)

        //set the items to your ItemAdapter
        //horizontalRecyclerView.itemAnimator = DefaultItemAnimator()
       // horizontalRecyclerView.adapter = fastAdapter
        //snapHelper.attachToRecyclerView(horizontalRecyclerView)
        val snapHelper = GravitySnapHelper(Gravity.START)
        snapHelper.attachToRecyclerView(horizontalRecyclerView)

        horizontalRecyclerView.layoutManager  = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        fastAdapter.notifyAdapterDataSetChanged()

        fastAdapter.addEventHook(object : ClickEventHook<SimpleItem>() {
            override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
                //return the views on which you want to bind this event
                return if (viewHolder is SimpleItem.ViewHolder) {
                    Log.e("viewHolder", viewHolder.toString())
                   viewHolder.itemView

                } else {
                    null
                }
            }

            override fun onClick(v: View, position: Int, fastAdapter: FastAdapter<SimpleItem>, item: SimpleItem) {
                //react on the click event
                productActivityViewModel.loadItem(product[position])
                findNavController().navigate(R.id.detail_fragment, null)
            }
        })
    }

    override fun onProductClick(position: Int) {
            productActivityViewModel.loadItem(product[position])
            findNavController().navigate(R.id.detail_fragment, null)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }
}
