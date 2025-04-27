package com.courage.vibestickers.repository

import android.graphics.Bitmap
import com.courage.vibestickers.data.model.StoryTypesData

interface StoryRepository {
    suspend fun getStoryTypes() : List<StoryTypesData>
    suspend fun getStoryImage(filePath: String) : Bitmap?
}