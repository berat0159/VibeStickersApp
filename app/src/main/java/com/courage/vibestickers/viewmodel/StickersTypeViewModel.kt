package com.courage.vibestickers.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.Bitmap
import com.courage.vibestickers.data.model.StickersType
import com.courage.vibestickers.repository.StickersRepository
import com.courage.vibestickers.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StickersTypeViewModel @Inject constructor(
    private val repository: StickersRepository,
    private val userRepository: UserRepository,
    private val firebaseAuth: FirebaseAuth
) :
    ViewModel() {
    private val _stickersType = MutableStateFlow<List<StickersType>>(emptyList())
    val stickersType: StateFlow<List<StickersType>> get() = _stickersType

    // Favori Type id'lerini tutuyoruz
    private val _favoriteTypes = MutableStateFlow<Set<String>>(emptySet())
    val favoriteTypes: StateFlow<Set<String>> = _favoriteTypes

    private val _filteredStickerTypes = MutableStateFlow<List<StickersType>>(emptyList())
    val filteredStickersType: StateFlow<List<StickersType>> = _filteredStickerTypes

    private val _stickerImages = MutableStateFlow<Map<String, Bitmap?>>(emptyMap())
    val stickerImages: StateFlow<Map<String, Bitmap?>> get() = _stickerImages

    private var currentUserId: String? = null
    init {
        firebaseAuth.addAuthStateListener { auth ->
            currentUserId = auth.currentUser?.uid
            if (currentUserId != null) {
                Log.d("StickersTypeVM", "User authenticated: $currentUserId. Loading favorites.")
                loadUserFavorites(currentUserId!!)
            } else {
                Log.d("StickersTypeVM", "User not authenticated. Clearing local favorites.")
                _favoriteTypes.value = emptySet() // Kullanıcı çıkış yaparsa favorileri temizle
            }
        }
        // Başlangıçta da kullanıcı durumunu kontrol et
        currentUserId = firebaseAuth.currentUser?.uid
        if (currentUserId != null) {
            loadUserFavorites(currentUserId!!)
        } else {
            Log.d("StickersTypeVM", "Initial check: User not authenticated.")
        }

        fetchStickersType() // Genel çıkartma tiplerini çek
    }




    private fun loadUserFavorites(userId: String) {
        userRepository.getFavoriteTypeIdsFlow(userId)
            .onEach { favoriteIds ->
                _favoriteTypes.value = favoriteIds
                Log.d("StickersTypeVM", "User favorites updated from Firestore: $favoriteIds for user $userId")
            }
            .catch { e ->
                Log.e("StickersTypeVM", "Error collecting favorites for user $userId", e)
                // Hata durumunda belki lokal favorileri boşalt
                _favoriteTypes.value = emptySet()
            }
            .launchIn(viewModelScope)
    }

    fun toggleFavorite(typeId: String) {
        val userId = currentUserId
        if (userId == null) {
            Log.w("StickersTypeVM", "User not logged in, cannot toggle favorite for $typeId.")
            // Belki kullanıcıyı giriş yapmaya yönlendir veya bir mesaj göster
            return
        }

        if (typeId.isEmpty()) {
            Log.w("StickersTypeVM", "Type ID is empty, cannot toggle favorite.")
            return
        }

        val isCurrentlyFavorite = _favoriteTypes.value.contains(typeId)


        viewModelScope.launch {
            try {
                if (isCurrentlyFavorite) {
                    userRepository.removeFavoriteTypeId(userId, typeId)
                    Log.d("StickersTypeVM", "Firestore: Removed $typeId from favorites for user $userId.")
                } else {
                    userRepository.addFavoriteTypeId(userId, typeId)
                    Log.d("StickersTypeVM", "Firestore: Added $typeId to favorites for user $userId.")
                }
            } catch (e: Exception) {
                Log.e("StickersTypeVM", "Error updating Firestore favorite for $typeId, user $userId", e)
                // Hata durumunda optimistic update'i geri al (eğer yaptıysan)
                // Veya en azından Flow'un bir sonraki emisyonunu bekle.
                // Şimdilik sadece logluyoruz.
            }
        }
    }


    fun isFavorite(typeId: String): Boolean {
        return _favoriteTypes.value.contains(typeId)
    }

    private fun fetchStickersType() {
        viewModelScope.launch {
            _stickersType.value = repository.getStickerTypes()
            _filteredStickerTypes.value = repository.getStickerTypes()
            _stickersType.value.forEach { stickerType ->
                fetchStickerImage(stickerType.typeImageUrl)
            }
        }
    }

    private fun fetchStickerImage(filePath: String) {
        viewModelScope.launch {
            val bitmap = repository.getStickerImage(filePath)
            _stickerImages.update { currentMap ->
                currentMap + (filePath to bitmap) // Yeni resmi ekle
            }
        }
    }

    fun filterStickerTypes(query: String) {
        _filteredStickerTypes.value = if (query.isBlank()) {
            _stickersType.value
        } else {
            _filteredStickerTypes.value.filter {
                it.typeName.contains(query.trim(), ignoreCase = true)
            }
        }
    }

}