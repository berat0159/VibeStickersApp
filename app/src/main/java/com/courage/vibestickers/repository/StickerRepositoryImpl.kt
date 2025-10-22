package com.courage.vibestickers.repository

import android.content.ContentValues
import android.content.Context
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.graphics.Bitmap
import android.util.Log
import com.courage.vibestickers.R
import com.courage.vibestickers.data.model.StickersType
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StickerRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : StickersRepository {
    override suspend fun getStickerTypes(): List<StickersType> {
        return try {
            val result = firestore.collection("stickerTypes")
                .get()
                .await()
                .toObjects(StickersType::class.java)
            Log.d("FirestoreDebug", "Fetched Sticker Types: $result") // Gelen veriyi logla
            result
        } catch (e: Exception) {
            Log.e("FirestoreDebug", "Error fetching sticker types", e)
            emptyList()
        }
    }


    override suspend fun getStickerImage(filePath: String): Bitmap? {
        return try {
            // Storage referansını doğru şekilde oluştur
            val storageRef = storage.reference.child(filePath)

            // Debug log'u ekle
            Log.d("StorageDebug", "Trying to download: $filePath")

            val bytes = storageRef.getBytes(5 * 1024 * 1024).await()
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size).also {
                Log.d("StorageDebug", "Successfully downloaded: $filePath")
            }
        } catch (e: Exception) {
            Log.e("StickerRepository", "Error downloading sticker image", e)
            null
        }
    }

}