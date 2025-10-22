package com.courage.vibestickers.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.courage.vibestickers.data.model.StickersType
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FavoriteStickersImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
):FavoriteStickersRepository {
    override suspend fun getFavoriteStickers(typeIds: List<String>): List<StickersType> {
        return try {
            val result = firestore.collection("stickerTypes")
                .whereIn("typeId", typeIds)
                .get()
                .await()
                .toObjects(StickersType::class.java)
            result
        }catch (e:Exception){
            emptyList()
        }
    }

    override suspend fun getFavoriteStickerImages(filePath: String): Bitmap? {
        return try {
            val storageRef = storage.reference.child(filePath)
            val bytes = storageRef.getBytes(5*1024*1024).await()
            BitmapFactory.decodeByteArray(bytes,0,bytes.size)
        }catch (e:Exception){
            null
        }
    }

}