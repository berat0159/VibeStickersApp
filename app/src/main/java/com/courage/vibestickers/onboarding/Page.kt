package com.courage.vibestickers.onboarding

import androidx.annotation.DrawableRes
import com.courage.vibestickers.R

data class Page(
    val tittle: String,
    val description: String,
    @DrawableRes val image: Int
)


val pageArray = arrayOf(
    Page(
        tittle = "Çıkartmaları Keşfet",
        description = "hazır animasyonlu çıkartmaların olduğu bir kütüphane ile tanış",
        image = R.drawable.boardingscreen1
    ),
    Page(
        tittle = "Kendi Çıkartmanı",
        description = "Kendi GIF'lerini veya animasyonlu çıkartmalarını oluştur",
        image = R.drawable.boardingscreen2
    ),
    Page(
        tittle = "Çıkartmalarını Paylaş",
        description = "Çıkartmalarını arkadaşlarınla paylaş",
        image = R.drawable.boardingscreen3
    )

)
