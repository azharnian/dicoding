package com.example.dicodingeventapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.dicodingeventapp.data.local.entity.FavoriteEventEntity

@Dao
interface FavoriteEventDao {

    @Query("SELECT * FROM favorite_event")
    fun getFavoriteEvents(): LiveData<List<FavoriteEventEntity>>

    @Query("SELECT * FROM favorite_event WHERE id = :id")
    fun getFavoriteById(id: Int): LiveData<FavoriteEventEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(event: FavoriteEventEntity)

    @Delete
    suspend fun deleteFavorite(event: FavoriteEventEntity)
}