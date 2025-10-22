package com.courage.vibestickers.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.layout.size
import androidx.lifecycle.viewmodel.compose.viewModel

import com.courage.vibestickers.data.model.CategoryDetailStickers
import com.courage.vibestickers.repository.CategoryDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryDetailViewModel @Inject constructor(private val repository: CategoryDetailRepository): ViewModel() {
    private val _categoryDetailStickers = MutableStateFlow<List<CategoryDetailStickers>>(emptyList())
    val categoryDetailStickers: StateFlow<List<CategoryDetailStickers>> get() = _categoryDetailStickers

    private val _categoryDetailImage = MutableStateFlow<Map<String,Bitmap?>>(emptyMap())
    val categoryDetailImage: StateFlow<Map<String,Bitmap?>> get() = _categoryDetailImage

    private val _saveStickerState = MutableStateFlow<Boolean?>(null)
    val saveStickerState: StateFlow<Boolean?> get() = _saveStickerState

    // private val _categoryStickerOnBoarding = MutableStateFlow<List<String>>(emptyList()) // ESKİ
    private val _categoryStickerOnBoarding = MutableStateFlow<Map<String, List<String>>>(emptyMap()) // YENİ: Map<TypeId, List<URL>>
    val categoryStickerOnBoarding: StateFlow<Map<String, List<String>>> get() = _categoryStickerOnBoarding

    
    init {
        _categoryDetailStickers.value.forEach {
            fetchCategoryDetailStickers(it.categoryId)
        }
    }


    // CategoryDetailViewModel.kt içinde
    fun fetchCategoryStickerOnBoarding(categoryId: String) { // categoryId aslında typeId olacak
        // Eğer bu categoryId için zaten veri varsa veya yükleniyorsa tekrar yükleme (basit bir kontrol)
        if (_categoryStickerOnBoarding.value.containsKey(categoryId)) {
            Log.d("CategoryVM", "Onboarding images for $categoryId already fetched or being fetched.")
            return
        }

        viewModelScope.launch {
            Log.d("CategoryVM", "Fetching onboarding images for categoryId: $categoryId")
            try {
                // Adım 1: Kategoriye ait tüm çıkartma detaylarını al (categoryImageUrl'leri içerir)
                val stickerDetailList = repository.getCategoryDetailSticker(categoryId) // Bu liste CategoryDetailStickers tipinde olmalı
                Log.d("CategoryVM", "Fetched ${stickerDetailList.size} stickers for category $categoryId")

                if (stickerDetailList.isEmpty()) {
                    _categoryStickerOnBoarding.update { currentMap ->
                        currentMap + (categoryId to emptyList()) // Boş liste ekle
                    }
                    Log.w("CategoryVM", "No stickers found for category $categoryId.")
                    return@launch
                }

                // Adım 2: İlk 3 (veya tüm) sticker'ın categoryImageUrl'lerinden (bunlar Storage yolu olmalı)
                // gerçek indirme URL'lerini al.
                val downloadUrlJobs = stickerDetailList
                    .take(3) // Sadece ilk 3'ünü alıyoruz
                    .mapNotNull { it.categoryImageUrl } // categoryImageUrl null veya boş değilse al
                    .filter { it.isNotBlank() }
                    .map { storagePath ->
                        async(Dispatchers.IO) { // Her bir URL'yi paralel olarak al
                            repository.getCategoryDownloadUrl(storagePath) // Bu fonksiyon String? döndürüyor
                        }
                    }

                val resolvedUrls = downloadUrlJobs.awaitAll().filterNotNull() // Başarılı olan ve null olmayan URL'leri al
                Log.d("CategoryVM", "Resolved ${resolvedUrls.size} URLs for $categoryId: $resolvedUrls")

                // Map’e ekle (öncekileri kaybetmeden)
                _categoryStickerOnBoarding.update { currentMap ->
                    currentMap + (categoryId to resolvedUrls)
                }

            } catch (e: Exception) {
                Log.e("CategoryVM", "Error fetching onboarding images for $categoryId", e)
                _categoryStickerOnBoarding.update { currentMap ->
                    currentMap + (categoryId to emptyList()) // Hata durumunda boş liste ekle
                }
            }
        }
    }


    fun fetchCategoryDetailStickers(categoryId:String){
        viewModelScope.launch {
            _categoryDetailStickers.value = repository.getCategoryDetailSticker(categoryId)
            _categoryDetailStickers.value.forEach { sticker ->
                fetchCategoryDetailImage(sticker.categoryImageUrl)
                Log.d("CategoryDetailViewModel22", "Fetched stickers:${sticker.categoryImageUrl}")
            }
        }

    }

    private fun fetchCategoryDetailImage(filePath: String) {
        viewModelScope.launch {
            Log.d("ImagePathDebug", "Trying to fetch image: '$filePath'")
            val bitmap = repository.getCategoryDetailImage(filePath)
            _categoryDetailImage.update { currentMap ->
                currentMap + (filePath to bitmap) // Yeni resmi ekle
            }
        }
    }

    fun makeDownloadStickers(context: Context){
        viewModelScope.launch {
            try {

                // mevcut  bitmap listesini topla
                val bitmaps = categoryDetailImage.value.values.filterNotNull()

                // repository e kaydetme işlemi yap
                repository.saveStickerToGallery(context = context, bitmaps = bitmaps)
                _saveStickerState.value = true
                Toast.makeText(context,"Paket Kaydedildi", Toast.LENGTH_LONG).show()
            }catch (e:Exception){
                _saveStickerState.value = false
            }

        }
    }

    fun shareSticker(context: Context,bitmap: Bitmap){
        viewModelScope.launch {
            repository.shareStickerDetailImage(context, bitmap)
        }

    }
}