package com.courage.vibestickers.repository

import android.graphics.BitmapFactory
import android.util.Log
import coil3.Bitmap
import com.courage.vibestickers.data.model.CategoryDetailStickers
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CategoryDetailStickerImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage

): CategoryDetailRepository {
    override suspend fun getCategoryDetailSticker(categoryId: String): List<CategoryDetailStickers> {
        return try {
            val stickers = firestore.collection("stickers")
                .whereEqualTo("categoryId",categoryId)
                .get()
                .await()
                .toObjects(CategoryDetailStickers::class.java)
            Log.d("StickerId", "Fetched stickers: $stickers")
            stickers
        }catch (e:Exception){
            Log.e("StickerIdError", "Error fetching stickers", e)
            emptyList()
        }
    }

    override suspend fun getCategoryDetailImage(filePath: String): Bitmap? {
        return try {
            val storageRef = storage.reference.child(filePath)

            Log.d("StickerImageUrl", "Trying to download: $filePath")

            val bytes = storageRef.getBytes(5 * 1024 * 1024).await()
            BitmapFactory.decodeByteArray(bytes,0,bytes.size).also {
                Log.d("CategoryDetailStickerImpl", "Successfully downloaded: $filePath")
            }
        }catch (e:Exception){
            Log.e("CategoryDetailStickerImpl2", "Error downloading image for $filePath: ${e.message}", e)
            null
            }

    }

}