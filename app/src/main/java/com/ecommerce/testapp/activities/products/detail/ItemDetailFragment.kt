package com.ecommerce.testapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController


class ItemDetailFragment : Fragment() {
    private lateinit var root:View
    private val productViewModel: ProductViewModel by activityViewModels()
    private var selectedSize:String = "M"
    private lateinit var selectedItem:ItemList

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        root = inflater.inflate(R.layout.fragment_item_detail, container, false)
        productViewModel.itemList.observe(viewLifecycleOwner, Observer<ItemList> { item ->
            selectedItem = item
            //product_name.text = item.name
        })

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
            productViewModel.insertUpdateCartItem(
                CartItem(
                    selectedItem.id,
                    selectedItem.name,
                    selectedItem.description,
                    selectedItem.price!!,
                    selectedItem.itemRate,
                    selectedSize
                )
            )
            findNavController().navigate(R.id.add_to_cart, null)
        }
        return root
    }
}
