package com.example.proyectoiniciarsesion2.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.example.proyectoiniciarsesion2.model.Marca
import com.example.proyectoiniciarsesion2.model.Modelo
import kotlinx.coroutines.tasks.await

class CarRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun getMarcas(userId: String): List<Marca> {
        return try {
            val snapshot = db.collection("marcas")
                .whereEqualTo("userId", userId)
                .get()
                .await()
            
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Marca::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun addMarca(marca: Marca): Result<Unit> {
        return try {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val marcaData = hashMapOf(
                    "nombre" to marca.nombre,
                    "año" to marca.año,
                    "pais" to marca.pais,
                    "userId" to currentUser.uid,
                    "createdAt" to System.currentTimeMillis(),
                    "updatedAt" to null
                )

                db.collection("marcas")
                    .add(marcaData)
                    .await()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Usuario no autenticado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateMarca(marca: Marca): Result<Unit> {
        return try {
            val marcaData = hashMapOf(
                "nombre" to marca.nombre,
                "año" to marca.año,
                "pais" to marca.pais,
                "userId" to marca.userId,
                "updatedAt" to System.currentTimeMillis()
            )

            db.collection("marcas")
                .document(marca.id)
                .update(marcaData as Map<String, Any>)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteMarca(marcaId: String): Result<Unit> {
        return try {
            db.collection("marcas")
                .document(marcaId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getModelos(userId: String): List<Modelo> {
        return try {
            val snapshot = db.collection("modelos")
                .whereEqualTo("userId", userId)
                .get()
                .await()
            
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Modelo::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun addModelo(modelo: Modelo): Result<Unit> {
        return try {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val modeloData = hashMapOf(
                    "nombre" to modelo.nombre,
                    "año" to modelo.año,
                    "precio" to modelo.precio,
                    "userId" to currentUser.uid,
                    "marcaId" to modelo.marcaId,
                    "createdAt" to System.currentTimeMillis()
                )

                db.collection("modelos")
                    .add(modeloData)
                    .await()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Usuario no autenticado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateModelo(modelo: Modelo): Result<Unit> {
        return try {
            val modeloData = hashMapOf(
                "nombre" to modelo.nombre,
                "año" to modelo.año,
                "precio" to modelo.precio,
                "userId" to modelo.userId,
                "marcaId" to modelo.marcaId,
                "updatedAt" to System.currentTimeMillis()
            )

            db.collection("modelos")
                .document(modelo.id)
                .set(modeloData)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteModelo(modeloId: String): Result<Unit> {
        return try {
            db.collection("modelos")
                .document(modeloId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}