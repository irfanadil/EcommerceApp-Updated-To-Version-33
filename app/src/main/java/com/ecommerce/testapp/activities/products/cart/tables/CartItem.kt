package com.ecommerce.testapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cartTable")
data class CartItem(
   //@PrimaryKey(autoGenerate = true) val auto_id:Int = 0,
    val cartItemId:Int?,
    val cartItemName:String?,
    val cartItemDescription:String?,
    var cartItemPrice:Int=0,
    val cartItemRate:Int?,
    val cartItemSize:String?,
    var cartItemQuantity:Int=1,
    @PrimaryKey(autoGenerate = true) var autoId:Int = 0
)
{

}



