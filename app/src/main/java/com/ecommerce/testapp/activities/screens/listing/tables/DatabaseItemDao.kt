package com.ecommerce.testapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// Not using this class....
@Dao
interface DatabaseItemDao {

    @Query("SELECT * from itemTable ORDER BY auto_id ASC")
    suspend fun getAllItems(): List<DatabaseItem>

    @Query("SELECT * FROM itemTable WHERE itemId=:id")
    fun getItemById(id: Int): DatabaseItem

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(club: DatabaseItem)

    @Query("DELETE FROM itemTable")
    suspend fun deleteAll()
}