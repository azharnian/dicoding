package com.example.dicodingeventapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_event")
data class FavoriteEventEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val mediaCover: String?,
    val ownerName: String?,
    val beginTime: String?,
    val description: String?,
    val quota: Int,
    val registrants: Int,
    val link: String?
)