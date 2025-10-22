package com.courage.vibestickers.repository

import android.content.Context
import coil3.Bitmap
import com.courage.vibestickers.R
import com.courage.vibestickers.data.model.StickersBanner
import com.courage.vibestickers.data.model.StickersType
import javax.inject.Inject

interface StickersRepository {
    suspend fun getStickerTypes() : List<StickersType>
    suspend fun getStickerImage(filePath: String) : Bitmap?

}


