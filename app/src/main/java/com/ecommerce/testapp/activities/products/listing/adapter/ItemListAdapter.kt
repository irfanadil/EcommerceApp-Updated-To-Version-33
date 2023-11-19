package com.ecommerce.testapp

import android.content.Context
import android.graphics.Point
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


const val totalItemsToBeDisplayed = 5
const val leftRightPadding = totalItemsToBeDisplayed * 6


class ItemListAdapter internal constructor(
    private var product: List<Product>,
    private var horizontalBox: Boolean,
    onProductListener: OnProductListener
) :  RecyclerView.Adapter<ItemListAdapter.BaseViewHolder>(){


    private val mOnProductListener: OnProductListener = onProductListener
    abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { abstract fun bind(
        item: Product
    ) }

    inner class ProductViewHolder(itemView: View, globalOnProductListener: OnProductListener) : BaseViewHolder(
        itemView
    ), View.OnClickListener {
        //val productImage:ImageView = itemView.product_image
        private val productName : TextView= itemView.findViewById(R.id.itemName)
        private val productDesc:TextView = itemView.findViewById(R.id.product_detail)
        private val productPrice:TextView = itemView.findViewById(R.id.product_price)
        private val productRating:RatingBar= itemView.findViewById(R.id.rating)
        val productImageView:ImageView = itemView.findViewById(R.id.product_image)
        private var onProductListener: OnProductListener

        init {
            itemView.setOnClickListener(this)
            this.onProductListener = globalOnProductListener
        }

        override fun onClick(v: View?) {
            Log.e("onClick - ", v.toString())
            mOnProductListener.onProductClick(adapterPosition)
        }

        override fun bind(item: Product) {
            productName.text = item.title
            productDesc.text = item.category
            productPrice.text = item.price.toString()
            //if(item.description!=null && item.description>0)
               // productRating.numStars = item.description
            item.image?.let {
                Glide.with(itemView.context)
                .load(it)
                .placeholder(R.drawable.police)
                .error(R.drawable.police)
                .into(productImageView);
            }

            //productImageView.loadImage("https://via.placeholder.com/150" )
        }
    }

    inner class HorizontalProductViewHolder(
        itemView: View,
        globalOnProductListener: OnProductListener
    ) : BaseViewHolder(itemView), View.OnClickListener {
        private val productImageView:ImageView = itemView.findViewById(R.id.product_image)
        private val productName : TextView= itemView.findViewById(R.id.itemName)
        private val productPrice:TextView = itemView.findViewById(R.id.product_price)
        private var onProductListener: OnProductListener
        init {
            itemView.setOnClickListener(this)
            this.onProductListener = globalOnProductListener
        }
        override fun bind(item: Product) {
            productName.text = item.title
             productPrice.text = item.price.toString()
            item.image?.let {
                Glide.with(itemView.context)
                    .load(it)
                    .placeholder(R.drawable.police)
                    .error(R.drawable.police)
                    .into(productImageView);
            }
        }
        override fun onClick(v: View?) { mOnProductListener.onProductClick(adapterPosition) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val holder: BaseViewHolder?
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
       if(viewType == R.layout.list_products) {
           holder =  ProductViewHolder(
               inflater.inflate(R.layout.list_products, parent, false),
               mOnProductListener
           )
       }
        else {
           holder= HorizontalProductViewHolder(
               inflater.inflate(
                   R.layout.list_products_horizontal,
                   parent,
                   false
               ), mOnProductListener
           )
           holder.itemView.layoutParams.width = (getScreenWidth(parent.context) / totalItemsToBeDisplayed)  /// THIS LINE WILL DIVIDE OUR VIEW INTO NUMBERS OF PARTS
        }
        return holder
    }

    fun getScreenWidth(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x
    }

    fun getScreenWidthTwo(context: Context): Int {
        var screenWidth = 1
        if (screenWidth === 0) {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            screenWidth = size.x
        }
        return screenWidth
    }

    override fun getItemViewType(position: Int): Int {
        return if(horizontalBox)
            R.layout.list_products_horizontal
        else
            R.layout.list_products

    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int)          {
        holder.bind(product[position])
    }

    internal fun setAllItems(product: List<Product>) {
        this.product = product
        notifyDataSetChanged()
    }

    override fun getItemCount() = product.size

    interface OnProductListener {
        fun onProductClick(position: Int)
    }

}