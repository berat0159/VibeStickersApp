package com.courage.vibestickers.viewmodel


import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.courage.vibestickers.data.model.CreatedStickers
import com.courage.vibestickers.repository.CreatedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Thread.State
import javax.inject.Inject

@HiltViewModel
class CreatedViewModel @Inject constructor(private val repository: CreatedRepository) : ViewModel() {

    private val _createdStickerImages = MutableStateFlow<List<String>>(emptyList())
    val createdStickerImages: StateFlow<List<String>> = _createdStickerImages

    private val _createdDetailStickers = MutableStateFlow<List<CreatedStickers>>(emptyList())
    val createdDetailStickers: StateFlow<List<CreatedStickers>> = _createdDetailStickers

    private val _createdWillDownloadImages = MutableStateFlow<Map<String,Bitmap>>(emptyMap())
    val createdWillDownloadImages: StateFlow<Map<String,Bitmap>> = _createdWillDownloadImages

    private val _saveStickerState = MutableStateFlow<Boolean?>(null)
    val saveStickerState: StateFlow<Boolean?> get() = _saveStickerState


    init {
        fetchCreatedStickers()
        fetchCreatedOnBoardingImage()
    }


    private fun fetchCreatedOnBoardingImage(){
        viewModelScope.launch {

            // firebasedeki verileri data class'a çevirilmiş halini alıyor
            val stickersList = repository.getCreatedStickers()

            // stickersList veri sınıfından ImageUrl yi almak için kullanıldı
            val listUrl = stickersList.mapNotNull { createdStickers ->
                repository.getDownloadUrl(createdStickers.createdImageUrl)

            }
            Log.d("CreatedList","$listUrl")
            // bu listeyi ui da göstermek için kullanıldı
            _createdStickerImages.value = listUrl
        }
    }



    // Her bitmapi ayrı coroutine ile indirip anında StateFlow'a ekliyoruz
    fun fetchCreatedStickers() {
        viewModelScope.launch {
            val stickersList = repository.getCreatedStickers() // Önce değişkene ata
            _createdDetailStickers.value = stickersList
            Log.d("CreatedDebug", "Fetched CreatedStickers list size: ${stickersList.size}")

            if (stickersList.isEmpty()) {
                Log.w("CreatedDebug", "CreatedStickers list is empty. No images will be fetched.")
            }
            stickersList.forEach { sticker -> // Güncellenmiş listeyi kullan
                Log.d("CreatedDebug", "Fetching image for CreatedImageUrl: ${sticker.createdImageUrl}")
                fetchCreatedDetailImage(sticker.createdImageUrl)
            }
        }
    }



        private fun fetchCreatedDetailImage(filePath: String) {
            viewModelScope.launch {
                Log.d("ImagePathDebug", "Trying to fetch image: '$filePath'")
                val bitmap = repository.getCreatedImages(filePath)
                if (bitmap != null) {
                    _createdWillDownloadImages.update { currentMap ->
                        currentMap + (filePath to bitmap) // Yeni resmi ekle
                    }
                }
            }
        }

        fun makeDownloadCreatedStickers(context: Context) {
            viewModelScope.launch {
                try {
                    Log.d("CreatedDebug", "Kaydetme işlemi başladı...")

                    val bitmaps = createdWillDownloadImages.value.values.toList()
                    Log.d("CreatedDebug", "Toplam bitmap sayısı: ${bitmaps.size}")

                    bitmaps.forEachIndexed { index, bitmap ->
                        Log.d(
                            "CreatedDebug",
                            "Bitmap #$index boyut: ${bitmap.width}x${bitmap.height}"
                        )
                    }

                    repository.saveStickersToGallery(context, bitmaps)

                    _saveStickerState.value = true
                    Log.d("CreatedDebug", "Kaydetme başarılı!")
                    Toast.makeText(context, "Paketler Kaydedildi", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    _saveStickerState.value = false
                    Log.e("CreatedDebug", "Kaydetme sırasında hata: ${e.message}", e)
                }
            }
        }


        fun addCreatedStickers(context: Context, croppedStickerBitmap: Bitmap?) {
            viewModelScope.launch {
                val result = repository.addCreatedStickerImage(
                    context = context,
                    croppedStickerBitmap = croppedStickerBitmap
                )
                result.onSuccess {
                    // Başarılı bir şekilde eklendi
                }.onFailure { e ->
                    // Hata durumunu ele al
                    println("Hata: ${e.message}")
                }
            }
        }
}