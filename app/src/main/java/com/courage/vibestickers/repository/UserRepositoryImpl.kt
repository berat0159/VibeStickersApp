package com.courage.vibestickers.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.webkit.internal.ApiHelperForM
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.io.path.exists

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
):UserRepository{
    companion object {
        private const val USERS_COLLECTION = "users"
        private const val FAVORITE_IDS_FIELD = "favoriteStickerTypeIds"
        private const val TAG = "UserRepositoryImpl"
    }
    override fun getFavoriteTypeIdsFlow(userId: String): Flow<Set<String>> = callbackFlow {
        if (userId.isEmpty()) {
            Log.w(TAG, "User ID is empty, emitting empty set for favorites.")
            trySend(emptySet())
            close() // No further updates if userId is empty
            return@callbackFlow
        }

        val userDocRef = firestore.collection(USERS_COLLECTION).document(userId)

        val listenerRegistration = userDocRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w(TAG, "Listen failed for user favorites.", error)
                close(error) // Flow'u hata ile kapat
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val idsList = snapshot.get(FAVORITE_IDS_FIELD) as? List<*> // Firestore list olarak döner
                val idsSet = idsList?.filterIsInstance<String>()?.toSet() ?: emptySet()
                Log.d(TAG, "Favorites updated from Firestore for user $userId: $idsSet")
                trySend(idsSet).isSuccess // Flow'a yeni veriyi gönder
            } else {
                Log.d(TAG, "User document for $userId does not exist or has no favorites, emitting empty set.")
                trySend(emptySet()).isSuccess // Boş liste gönder
            }
        }
        awaitClose {
            Log.d(TAG, "Closing Firestore listener for user $userId favorites.")
            listenerRegistration.remove()
        }
    }

    override suspend fun addFavoriteTypeId(userId: String, typeId: String) {
        if (userId.isEmpty() || typeId.isEmpty()) {
            Log.w(TAG, "Cannot add favorite: User ID or Type ID is empty.")
            return
        }
        val userDocRef = firestore.collection(USERS_COLLECTION).document(userId)
        try {
            // Önce dökümanın var olup olmadığını kontrol et, yoksa oluştur ve sonra güncelle
            val docSnapshot = userDocRef.get().await()
            if (!docSnapshot.exists()) {
                // Döküman yoksa, yeni bir döküman oluştur ve favori ID'yi ekle
                userDocRef.set(mapOf(FAVORITE_IDS_FIELD to listOf(typeId)),
                    SetOptions.merge()).await()
                Log.d(TAG, "Created user document and added favorite $typeId for user $userId")
            } else {
                // Döküman varsa, favori ID'yi arrayUnion ile ekle
                userDocRef.update(FAVORITE_IDS_FIELD, FieldValue.arrayUnion(typeId)).await()
                Log.d(TAG, "Added favorite $typeId for user $userId")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error adding favorite $typeId for user $userId", e)
            // Hata durumunda, özellikle döküman yoksa ve set işlemi de başarısız olursa,
            // burada daha robust bir hata yönetimi eklenebilir.
            // Örneğin, SetOptions.merge() ile doğrudan set denenebilir.
            // Alternatif olarak, ilk denemede doğrudan set(mapOf(...), SetOptions.merge()) yapılabilir.
            try {
                userDocRef.set(mapOf(FAVORITE_IDS_FIELD to FieldValue.arrayUnion(typeId)),
                    SetOptions.merge()).await()
                Log.d(TAG, "Retry: Set (with merge) favorite $typeId for user $userId after error.")
            } catch (retryException: Exception) {
                Log.e(TAG, "Retry failed for adding favorite $typeId for user $userId", retryException)
            }
        }
    }

    override suspend fun removeFavoriteTypeId(userId: String, typeId: String) {
        if (userId.isEmpty() || typeId.isEmpty()) {
            Log.w(TAG, "Cannot remove favorite: User ID or Type ID is empty.")
            return
        }
        val userDocRef = firestore.collection(USERS_COLLECTION).document(userId)
        try {
            userDocRef.update(FAVORITE_IDS_FIELD, FieldValue.arrayRemove(typeId)).await()
            Log.d(TAG, "Removed favorite $typeId for user $userId")
        } catch (e: Exception) {
            Log.e(TAG, "Error removing favorite $typeId for user $userId", e)
        }    }
}