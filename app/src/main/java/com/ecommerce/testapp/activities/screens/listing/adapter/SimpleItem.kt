package com.ecommerce.testapp.activities.screens.listing.adapter

import android.content.Context
import android.graphics.Point
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

import com.ecommerce.testapp.R.*
import com.ecommerce.testapp.totalItemsToBeDisplayed
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem

open class SimpleItem : AbstractItem<SimpleItem.ViewHolder>() {
    var name: String? = null
    var price: String? = null
    var sampleImage: String? = null
    var imageString: String? = null

    /** defines the type defining this item. must be unique. preferably an id */

    override val type: Int = 1

    /** defines the layout which will be used for this item in the list */
    override val layoutRes: Int
        get() = layout.list_products_horizontal

    override fun getViewHolder(v: View): ViewHolder {
        v.layoutParams.width = (getScreenWidth(v.context) / totalItemsToBeDisplayed)
        return ViewHolder(v)
    }

    fun getScreenWidth(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x
    }

    class ViewHolder(view: View) : FastAdapter.ViewHolder<SimpleItem>(view) {
        var name: TextView = view.findViewById(id.itemName)
        var price: TextView = view.findViewById(id.product_price)
        var imageView:ImageView = view.findViewById(id.product_image)

        override fun bindView(item: SimpleItem, payloads: List<Any>) {
            name.text = item.name
            price.text = item.price
            item.imageString?.let {
                Glide.with(itemView.context)
                    .load(it)
                    //.placeholder(R.drawable.police)
                    //.error(R.drawable.police)
                    .into(imageView);
            }
        }

        override fun unbindView(item: SimpleItem) {
            name.text = null
            price.text = null

        }
    }
}