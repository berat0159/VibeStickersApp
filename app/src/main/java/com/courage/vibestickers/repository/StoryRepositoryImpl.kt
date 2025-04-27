package com.courage.vibestickers.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.courage.vibestickers.R
import com.courage.vibestickers.data.model.StoryTypesData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class StoryRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : StoryRepository {
    override suspend fun getStoryTypes(): List<StoryTypesData> {
        return try {
            val storyTypesFireStore = firestore.collection("storyTypes")
                .get()
                .await()
                .toObjects(StoryTypesData::class.java)

            Log.d("FirestoreDebug", "Fetched Story Types: $storyTypesFireStore") // Gelen veriyi logla
            storyTypesFireStore

        }catch (e: Exception){
            Log.d("FirestoreDebug", "Error fetching story types: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getStoryImage(filePath: String): Bitmap? {
        return try {
            val storageRef = storage.reference.child(filePath)

            val bytes = storageRef.getBytes(5 * 1024 * 1024).await()
            BitmapFactory.decodeByteArray(bytes,0,bytes.size).also {
                Log.d("StorageDebug", "Successfully downloaded: $filePath")
            }

        }catch (e: Exception){
            Log.e("StoryRepository", "Error downloading story image", e)
            null

        }
    }

}