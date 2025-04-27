package com.courage.vibestickers.data.model

import com.courage.vibestickers.R

data class StickersType(
    val typeId:String="",
    val typeName:String = "",
    val typeImageUrl:String = ""
)

data class CategoryDetailStickers(
    val categoryId:String="",
    val categoryTittle:String="",
    val stickerImageUrl:String = ""
)

data class StickersBanner(
    val bannerImage:Int,
    val bannerTitle:String,
)


val bannerList = listOf(
    StickersBanner(bannerImage = R.drawable.stickerlogo, bannerTitle = "Banner 1") ,
    StickersBanner(bannerImage = R.drawable.stickerlogo, bannerTitle = "Banner 2")
)

