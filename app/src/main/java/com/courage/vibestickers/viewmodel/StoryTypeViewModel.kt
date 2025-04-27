package com.courage.vibestickers.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.courage.vibestickers.data.model.StoryTypesData
import com.courage.vibestickers.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryTypeViewModel @Inject constructor(private val repository: StoryRepository) : ViewModel() {
        private val _storyTypes = MutableStateFlow<List<StoryTypesData>>(emptyList())
    val storyType: StateFlow<List<StoryTypesData>> get() = _storyTypes

    private val _storyImage = MutableStateFlow<Map<String, Bitmap?>>(emptyMap())
    val storyImage: StateFlow<Map<String, Bitmap?>> get() = _storyImage


    init {
        fetchStoryTypes()
    }

    private fun fetchStoryTypes() {
        viewModelScope.launch {
            _storyTypes.value = repository.getStoryTypes()
            _storyTypes.value.forEach{ storyType ->
                fetchStoryImage(storyType.storyImageUrl)
            }
        }
    }

    private fun fetchStoryImage(filePath: String) {
        viewModelScope.launch {
            val bitmap = repository.getStoryImage(filePath)
            _storyImage.update { currentMap ->
                currentMap + (filePath to bitmap)
            }
        }
    }
}