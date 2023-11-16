package com.ecommerce.testapp

import androidx.room.Entity
import androidx.room.PrimaryKey

// Not using this class....
@Entity(tableName = "itemTable")
data class DatabaseItem(
    val itemId:Int?,
    val itemName:String?,
    val description:String?,
    val price:Int?,
    val itemRate:Int?,
    @PrimaryKey(autoGenerate = true) val auto_id:Int
)

