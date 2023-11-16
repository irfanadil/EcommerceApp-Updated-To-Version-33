package com.ecommerce.testapp

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ecommerce.testapp.activities.products.listing.adapter.SimpleItem
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.golfercard.playsafe.ItemListingFragmentViewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook


class ItemListingFragment : Fragment() , ItemListAdapter.OnProductListener {

    //private lateinit var itemListingFragmentViewModel: ItemListingFragmentViewModel
    private val itemListingFragmentViewModel: ItemListingFragmentViewModel by activityViewModels()
    private lateinit var adapter:ItemListAdapter
    //private lateinit var horizontalAdapter:ItemListAdapter
    private lateinit var recyclerView: RecyclerView
    private  var  itemList: List<ItemList> = listOf()

    private lateinit var horizontalRecyclerView: RecyclerView
    private lateinit var root:View

    private lateinit var loader:ProgressBar

    private val productActivityViewModel: ProductViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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




        itemListingFragmentViewModel.itemListResult.observe(requireActivity(), Observer {
            loader.visibility = View.GONE
            if (it.itemList != null && it.itemList.isNotEmpty()) {
                itemList = it.itemList
                adapter.setAllItems(itemList)
                //horizontalAdapter.setAllItems(itemList)
                adapter.notifyDataSetChanged()
                //horizontalAdapter.notifyDataSetChanged()

                //When data is loaded...
                setUpHorizontalFastAdapter(itemList)

            } else
                Toast.makeText(requireContext(), "No products found...", Toast.LENGTH_LONG).show()
        })



        itemListingFragmentViewModel.refresh()
        return root
    }

    private fun setUpHorizontalFastAdapter(localItemList:List<ItemList>)
    {
        val simpleItemsList: ArrayList<SimpleItem> = ArrayList()

        for (eachItem in localItemList) {
            Log.e("eachItem", eachItem.name +" "+eachItem.price)
            val simpleItem = SimpleItem()
            simpleItem.name = eachItem.name
            simpleItem.price = eachItem.price.toString()
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
                productActivityViewModel.loadItem(itemList[position])
                findNavController().navigate(R.id.detail_fragment, null)
            }
        })
    }

    override fun onProductClick(position: Int) {
            productActivityViewModel.loadItem(itemList[position])
            findNavController().navigate(R.id.detail_fragment, null)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }
}
