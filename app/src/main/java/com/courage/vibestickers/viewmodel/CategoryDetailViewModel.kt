package com.courage.vibestickers.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.Bitmap
import com.courage.vibestickers.data.model.CategoryDetailStickers
import com.courage.vibestickers.repository.CategoryDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryDetailViewModel @Inject constructor(private val repository: CategoryDetailRepository): ViewModel() {
    private val _categoryDetailStickers = MutableStateFlow<List<CategoryDetailStickers>>(emptyList())
    val categoryDetailStickers: StateFlow<List<CategoryDetailStickers>> get() = _categoryDetailStickers

    private val _categoryDetailImage = MutableStateFlow<Map<String,Bitmap?>>(emptyMap())
    val categoryDetailImage: StateFlow<Map<String,Bitmap?>> get() = _categoryDetailImage


    init {
        _categoryDetailStickers.value.forEach {
            fetchCategoryDetailStickers(it.categoryId)
        }
    }


    fun fetchCategoryDetailStickers(categoryId:String){
        viewModelScope.launch {
            _categoryDetailStickers.value = repository.getCategoryDetailSticker(categoryId)
            _categoryDetailStickers.value.forEach { sticker ->
                fetchCategoryDetailImage(sticker.stickerImageUrl)
                Log.d("CategoryDetailViewModel22", "Fetched stickers:${sticker.stickerImageUrl}")
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
}