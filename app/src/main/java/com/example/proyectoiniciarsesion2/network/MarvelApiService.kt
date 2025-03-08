package com.example.proyectoiniciarsesion2.network

import com.example.proyectoiniciarsesion2.model.MarvelResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelApiService {
    @GET("v1/public/characters")
    suspend fun getCharacters(
        @Query("apikey") apiKey: String,
        @Query("ts") timestamp: String,
        @Query("hash") hash: String,
        @Query("limit") limit: Int = 20
    ): MarvelResponse

    companion object {
        const val BASE_URL = "https://gateway.marvel.com/"
        const val PUBLIC_KEY = "66ea6ad689b2d220fbdd6d78c9f644b0"
        const val PRIVATE_KEY = "b826a207de942728b01e04c9e8cb3c3b2c5798e4"
    }
} 