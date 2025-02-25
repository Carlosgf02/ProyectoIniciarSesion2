package com.example.proyectoiniciarsesion2.model

data class Marca(
    val id: String = "",
    val nombre: String = "",
    val año: Int = 0,
    val pais: String = "",
    val userId: String = "",
    val createdAt: Long = 0,
    val updatedAt: Long? = null
)