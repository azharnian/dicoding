package com.example.dicodingeventapp.data.api

import com.example.dicodingeventapp.data.model.EventResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("events")
    suspend fun getEvents(
        @Query("active") active: Int
    ): EventResponse

    @GET("events")
    suspend fun searchEvents(
        @Query("active") active: Int,
        @Query("q") query: String
    ): EventResponse

    @GET("events")
    suspend fun getLatestEvent(
        @Query("active") active: Int,
        @Query("limit") limit: Int
    ): EventResponse

}