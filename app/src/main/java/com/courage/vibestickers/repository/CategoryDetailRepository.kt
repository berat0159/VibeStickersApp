package com.courage.vibestickers.repository

import coil3.Bitmap
import com.courage.vibestickers.data.model.CategoryDetailStickers

interface CategoryDetailRepository {
    suspend fun getCategoryDetailSticker(categoryId:String) : List<CategoryDetailStickers>
    suspend fun getCategoryDetailImage(filePath: String): Bitmap?
}