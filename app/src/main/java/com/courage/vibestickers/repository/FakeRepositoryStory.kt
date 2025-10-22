package com.courage.vibestickers.repository

import android.graphics.Bitmap
import com.courage.vibestickers.data.model.StoryTypesData

class FakeRepositoryStory: StoryRepository {
    override suspend fun getStoryTypes(): List<StoryTypesData> {
        TODO("Not yet implemented")
    }

    override suspend fun getStoryImage(filePath: String): Bitmap? {
        TODO("Not yet implemented")
    }
}