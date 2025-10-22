package com.courage.vibestickers.repository

import coil3.Bitmap
import com.courage.vibestickers.data.model.StickersType

class FakeRepositoryStickersType: StickersRepository {
    override suspend fun getStickerTypes(): List<StickersType> {
        TODO("Not yet implemented")
    }

    override suspend fun getStickerImage(filePath: String): Bitmap? {
        TODO("Not yet implemented")
    }

}