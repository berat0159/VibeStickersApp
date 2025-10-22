package com.courage.vibestickers.data.model

import com.courage.vibestickers.R
import kotlinx.serialization.Serializable

data class StickersType(
    val typeId:String="",
    val typeName:String = "",
    val typeImageUrl:String = ""
)

data class CategoryDetailStickers(
    val categoryId:String="",
    val categoryTittle:String="",
    val categoryImageUrl:String = ""
)

data class StickersBanner(
    val bannerImage:Int,
    val bannerTitle:String,
)

val bannerList = listOf(
    StickersBanner(bannerImage = R.drawable.banner1, bannerTitle = "Banner 1") ,
    StickersBanner(bannerImage = R.drawable.banner2, bannerTitle = "Banner 2")
)


data class CreatedStickers(
    var createdId:String="",
    var createdImageUrl:String = ""
)


