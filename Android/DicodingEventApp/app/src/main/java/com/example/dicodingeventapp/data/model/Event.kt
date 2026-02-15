package com.example.dicodingeventapp.data.model

import com.google.gson.annotations.SerializedName

data class Event(

    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("imageLogo")
    val imageLogo: String,

    @SerializedName("ownerName")
    val ownerName: String,

    @SerializedName("beginTime")
    val beginTime: String,

    @SerializedName("quota")
    val quota: Int,

    @SerializedName("registrants")
    val registrants: Int,

    @SerializedName("description")
    val description: String,

    @SerializedName("link")
    val link: String
)