package com.courage.vibestickers.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.courage.vibestickers.data.model.StickersType
import com.courage.vibestickers.repository.FavoriteStickersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import java.lang.Thread.State
import javax.inject.Inject
// FavoriteStickersViewModel.kt

@HiltViewModel
class FavoriteStickersViewModel @Inject constructor(private val repository: FavoriteStickersRepository) : ViewModel() {
    private val _favoriteStickers = MutableStateFlow<List<StickersType>>(emptyList())
    val favoriteStickers: StateFlow<List<StickersType>> = _favoriteStickers

    private val _favoriteStickerImages = MutableStateFlow<Map<String, Bitmap?>>(emptyMap())
    val favoriteStickerImage: StateFlow<Map<String, Bitmap?>> = _favoriteStickerImages


    private val _favoriteStickerOnBoarding = MutableStateFlow<Map<String, List<String>>>(emptyMap())
    val favoriteStickerOnBoarding: StateFlow<Map<String, List<String>>> = _favoriteStickerOnBoarding

    // Yükleme durumunu takip etmek için
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading




    fun fetchFavoriteStickers(typeIds: List<String>) {
        if (typeIds.isEmpty()) { // Ekstra kontrol
            clearFavoriteStickers()
            return
        }
        viewModelScope.launch {
            _isLoading.value = true // Yükleme başladı
            try {
                Log.d("FavoriteVM", "Fetching stickers for IDs: $typeIds")
                val resultFavoriteById = repository.getFavoriteStickers(typeIds)
                _favoriteStickers.value = resultFavoriteById
                Log.d("FavoriteVM", "Fetched ${resultFavoriteById.size} stickers. Images next...")
                // Resimleri paralel olarak indirmek daha verimli olabilir
                val imageJobs = mutableListOf<Job>()
                resultFavoriteById.forEach { stickerType ->
                    stickerType.typeImageUrl.let { imageUrl -> // Null kontrolü eklendi
                        if (!_favoriteStickerImages.value.containsKey(imageUrl) || _favoriteStickerImages.value[imageUrl] == null) {
                            imageJobs += launch { // Her resim için ayrı bir coroutine
                                fetchFavoriteStickerImage(imageUrl)
                            }
                        }
                    } ?: Log.w("FavoriteVM", "typeImageUrl is null for typeId: ${stickerType.typeId}")
                }
                imageJobs.joinAll() // Tüm resim indirme işlerinin bitmesini bekle (opsiyonel)
            } catch (e: Exception) {
                Log.e("FavoriteVM", "Error in fetchFavoriteStickers", e)
                _favoriteStickers.value = emptyList() // Hata durumunda listeyi temizle
            } finally {
                _isLoading.value = false // Yükleme bitti
            }
        }
    }

    private suspend fun fetchFavoriteStickerImage(filePath: String) { // suspend yapıldı
        // filePath null ise erken çık (yukarıda kontrol eklendi ama burada da olabilir)
        // if (filePath == null) return

        try {
            Log.d("FavoriteVM", "Fetching image for: $filePath")
            val bitmap = repository.getFavoriteStickerImages(filePath)
            if (bitmap != null) {
                _favoriteStickerImages.update { currentMap ->
                    currentMap + (filePath to bitmap)
                }
                Log.d("FavoriteVM", "Image fetched for: $filePath")
            } else {
                Log.w("FavoriteVM", "Bitmap is null for: $filePath")
            }
        } catch (e: Exception) {
            Log.e("FavoriteVM", "Error fetching image $filePath", e)
        }
    }

    private fun clearFavoriteStickers() {
        Log.d("FavoriteVM", "Clearing favorite stickers and images.")
        _favoriteStickers.value = emptyList()
        _favoriteStickerImages.value = emptyMap()
        _isLoading.value = false // Yükleme durumunu da sıfırla
    }
}
