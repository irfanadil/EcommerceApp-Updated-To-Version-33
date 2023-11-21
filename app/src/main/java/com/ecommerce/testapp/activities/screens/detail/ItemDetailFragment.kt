package com.ecommerce.testapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.ecommerce.testapp.activities.MainViewModel


class ItemDetailFragment : Fragment() {
    private lateinit var root:View
    private val mainViewModel: MainViewModel by activityViewModels()
    private var selectedSize:String = "M"
    private lateinit var selectedItem:Product

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        root = inflater.inflate(R.layout.fragment_item_detail, container, false)
        val cartImageView = root.findViewById<ImageView>(R.id.cartImage)
        val itemName = root.findViewById<TextView>(R.id.itemName)
        mainViewModel.product.observe(viewLifecycleOwner) { item ->
            selectedItem = item
            item.title?.let {
                itemName.text = it
            }
            item.image?.let {
                Glide.with(root.context)
                    .load(it)
                    .placeholder(R.drawable.police)
                    .error(R.drawable.police)
                    .into(cartImageView);
            }
            //product_name.text = item.name
        }

        root.findViewById<Button>(R.id.materialButton).setOnClickListener {
            //selectedSize = materialButton.text.toString()
        }
        root.findViewById<Button>(R.id.materialButton2).setOnClickListener {
            //selectedSize = materialButton2.text.toString()
        }
        root.findViewById<Button>(R.id.materialButton3).setOnClickListener {
           // selectedSize = materialButton3.text.toString()
        }
        root.findViewById<Button>(R.id.materialButton6).setOnClickListener {
           // selectedSize = materialButton6.text.toString()
        }

        root.findViewById<Button>(R.id.next_button).setOnClickListener {
            mainViewModel.insertUpdateCartItem(
                CartItem(
                    cartItemId = selectedItem.id,
                    cartItemName = selectedItem.title,
                    cartImage = selectedItem.image,
                    cartItemCategory = selectedItem.category,
                    cartItemPrice = selectedItem.price!!,
                    cartItemDescription = selectedItem.description,
                )
            )
            findNavController().navigate(R.id.add_to_cart, null)
        }
        return root
    }
}
