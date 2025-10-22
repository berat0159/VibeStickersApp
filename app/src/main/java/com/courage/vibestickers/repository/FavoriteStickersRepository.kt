package com.courage.vibestickers.repository

import android.graphics.Bitmap
import com.courage.vibestickers.data.model.StickersType

interface FavoriteStickersRepository {
    suspend fun getFavoriteStickers(typeIds: List<String>): List<StickersType>
    suspend fun getFavoriteStickerImages(filePath:String): Bitmap?
}