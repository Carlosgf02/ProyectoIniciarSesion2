package com.example.proyectoiniciarsesion2.model

import com.google.gson.annotations.SerializedName

data class MarvelResponse(
    @SerializedName("data")
    val data: MarvelData
)

data class MarvelData(
    @SerializedName("results")
    val results: List<Character>
)

data class Character(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("thumbnail")
    val thumbnail: Image
)

data class Image(
    @SerializedName("path")
    val path: String,
    @SerializedName("extension")
    val extension: String
) 