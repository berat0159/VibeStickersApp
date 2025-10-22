package com.courage.vibestickers.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.courage.vibestickers.data.model.CreatedStickers

interface CreatedRepository {
    suspend fun getCreatedStickers(): List<CreatedStickers>
    suspend fun getCreatedImages(filePath: String): Bitmap?
    suspend fun getDownloadUrl(filePath: String): String?
    suspend fun saveStickersToGallery(context: Context, bitmaps: List<Bitmap>)
    suspend fun deleteCreatedSticker(stickerId: String): Result<Unit>
    suspend fun addCreatedStickerImage(context: Context,croppedStickerBitmap: Bitmap?): Result<Unit>
}