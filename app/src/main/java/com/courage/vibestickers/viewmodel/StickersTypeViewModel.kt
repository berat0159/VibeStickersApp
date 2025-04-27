package com.courage.vibestickers.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.Bitmap
import com.courage.vibestickers.data.model.StickersType
import com.courage.vibestickers.repository.StickersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StickersTypeViewModel @Inject constructor(private val repository: StickersRepository) :ViewModel() {
    private val _stickersType = MutableStateFlow<List<StickersType>>(emptyList())
    val stickersType: StateFlow<List<StickersType>> get() = _stickersType

    private val _stickerImages = MutableStateFlow<Map<String,Bitmap?>>(emptyMap())
    val stickerImages: StateFlow<Map<String,Bitmap?>> get() = _stickerImages
    init {
        fetchStickersType()
    }

    private fun fetchStickersType() {
        viewModelScope.launch {
            _stickersType.value = repository.getStickerTypes()
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

}