package com.courage.vibestickers.repository

import android.content.Context
import android.graphics.Bitmap
import com.courage.vibestickers.data.model.CategoryDetailStickers

interface CategoryDetailRepository {
    suspend fun getCategoryDetailSticker(categoryId:String) : List<CategoryDetailStickers>
    suspend fun getCategoryDetailImage(filePath: String): Bitmap?
    suspend fun saveStickerToGallery(context: Context, bitmaps:List<Bitmap>)
    suspend fun shareStickerDetailImage(context: Context,bitmap:Bitmap)
    suspend fun getCategoryDownloadUrl(filePath: String): String?
}