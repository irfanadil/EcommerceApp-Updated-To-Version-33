package com.ecommerce.testapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartFragment : Fragment() , CartItemListAdapter.OnCartItemClickListener {

    private lateinit var adapter:CartItemListAdapter
    private lateinit var recyclerView: RecyclerView
    private  var  cartItemList: List<CartItem> = listOf()
    private lateinit var root:View
    private lateinit var backToListing:Button
    private lateinit var loader: ProgressBar
    private val productViewModel: ProductViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        root = inflater.inflate(R.layout.fragment_add_to_cart, container, false)
        loader = root.findViewById<ProgressBar>(R.id.cart_items_loader)
        loader.visibility = View.VISIBLE
        adapter = CartItemListAdapter(emptyList(),this)
        recyclerView= root.findViewById(R.id.cartItemsRecycleView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
        backToListing = root.findViewById(R.id.back_to_listing)
        backToListing.setOnClickListener {
            productViewModel.logoutUser()
        }

        productViewModel.cartItems.observe(viewLifecycleOwner, Observer<List<CartItem>> {
            var total:Int = 0
            for (item in it)
                total += item.cartItemPrice
            loader.visibility = View.GONE
            cartItemList = if(it != null && it.isNotEmpty()) it else listOf()
            adapter.setAllCartItems(cartItemList)
            adapter.notifyDataSetChanged()
            Toast.makeText(requireActivity(), "Total price $total", Toast.LENGTH_LONG ).show()
        })
        return root
    }
    override fun onCartItemRemoved(position: Int) =  productViewModel.updateCartItem(cartItemList[position])
    override fun onCartItemAdded(position: Int) = productViewModel.addCartItem(cartItemList[position])
}
