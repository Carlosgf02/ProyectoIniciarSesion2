package com.example.proyectoiniciarsesion2.model

data class Modelo(
    val id: String = "",
    val nombre: String = "",
    val año: Int = 0,
    val precio: Float = 0f,
    val userId: String = "",
    val marcaId: String = "",
    val createdAt: Long = 0,
    val updatedAt: Long? = null
)