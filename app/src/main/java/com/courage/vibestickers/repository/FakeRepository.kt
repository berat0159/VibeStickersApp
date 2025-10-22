package com.courage.vibestickers.repository

import android.content.Context
import android.graphics.Bitmap
import com.courage.vibestickers.data.model.CategoryDetailStickers

class FakeRepository : CategoryDetailRepository {
    override suspend fun getCategoryDetailSticker(categoryId: String): List<CategoryDetailStickers> {
        TODO("Not yet implemented")
    }

    override suspend fun getCategoryDetailImage(filePath: String): Bitmap? {
        TODO("Not yet implemented")
    }

    override suspend fun saveStickerToGallery(context: Context, bitmaps: List<Bitmap>) {
        TODO("Not yet implemented")
    }

    override suspend fun shareStickerDetailImage(context: Context, bitmap: Bitmap) {
        TODO("Not yet implemented")
    }

    override suspend fun getCategoryDownloadUrl(filePath: String): String? {
        TODO("Not yet implemented")
    }
}