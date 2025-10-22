package com.courage.vibestickers.repository

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import com.courage.vibestickers.data.model.CategoryDetailStickers
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class CategoryDetailStickerImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage

): CategoryDetailRepository {
    override suspend fun getCategoryDetailSticker(categoryId: String): List<CategoryDetailStickers> {
        return try {
            val stickers = firestore.collection("stickers")
                .whereEqualTo("categoryId",categoryId)
                .get()
                .await()
                .toObjects(CategoryDetailStickers::class.java)
            Log.d("StickerId", "Fetched stickers: $stickers")
            stickers
        }catch (e:Exception){
            Log.e("StickerIdError", "Error fetching stickers", e)
            emptyList()
        }
    }

    override suspend fun getCategoryDetailImage(filePath: String): Bitmap? {
        return try {
            val storageRef = storage.reference.child(filePath)

            Log.d("StickerImageUrl", "Trying to download: $filePath")

            val bytes = storageRef.getBytes(5 * 1024 * 1024).await()
            BitmapFactory.decodeByteArray(bytes,0,bytes.size).also {
                Log.d("CategoryDetailStickerImpl", "Successfully downloaded: $filePath")
            }
        }catch (e:Exception){
            Log.e("CategoryDetailStickerImpl2", "Error downloading image for $filePath: ${e.message}", e)
            null
            }

    }

    // Bu fonksiyon, verilen bitmap listesini cihazın galerisine kaydeder.
    // 'suspend' anahtar kelimesi, bu fonksiyonun bir korutin içinde çağrılması gerektiğini belirtir,
    // çünkü dosya işlemleri potansiyel olarak zaman alıcı olabilir ve ana thread'i bloke etmemelidir.
    override suspend fun saveStickerToGallery(context: Context, bitmaps: List<Bitmap>) {
        // ContentResolver, uygulamanın diğer uygulamaların verilerine (örneğin Medya Deposu)
        // erişmesini sağlayan bir arayüzdür.
        val resolver = context.contentResolver

        // Kaydedilecek sticker'ların galeri içinde bulunacağı klasörün adı.
        // Bu, genellikle "Pictures" klasörünün altında bir alt klasör olarak oluşturulur.
        val directory = "MyStickers"

        // Verilen bitmap listesindeki her bir bitmap için döngü başlatılır.
        // 'forEachIndexed' hem öğeyi (bitmap) hem de onun indeksini (index) sağlar.
        bitmaps.forEachIndexed{ index, bitmap ->

            // Her bir sticker için benzersiz bir dosya adı oluşturulur.
            // System.currentTimeMillis() kullanılarak zaman damgası eklenir, bu da çakışmaları önler.
            // 'index' de eklenerek aynı anda birden fazla bitmap kaydedilirse ayırt edilebilirlik sağlanır.
            val fileName = "sticker_${System.currentTimeMillis()}_${index}.png"

            // ContentValues, Medya Deposu'na eklenecek yeni medya öğesi için
            // meta verileri (dosya adı, MIME türü, göreli yol vb.) tutar.
            val contentValues = ContentValues().apply {
                // MediaStore.MediaColumns.DISPLAY_NAME: Dosyanın kullanıcıya gösterilecek adı.
                put(MediaStore.MediaColumns.DISPLAY_NAME,fileName)
                // MediaStore.MediaColumns.MIME_TYPE: Dosyanın türünü belirtir (bu durumda PNG resmi).
                put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                // MediaStore.MediaColumns.RELATIVE_PATH: Dosyanın galeri içindeki göreli yolunu belirtir.
                // Android Q (API 29) ve sonrası için standart bir yoldur.
                // Örnek: Pictures/MyStickers/sticker_12345.png
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/$directory")
            }

            // resolver.insert() metodu, Medya Deposu'na yeni bir öğe ekler ve
            // bu öğeye erişmek için kullanılacak bir URI döndürür.
            // MediaStore.Images.Media.EXTERNAL_CONTENT_URI, harici depolamadaki resimler tablosunu işaret eder.
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            // URI null değilse (yani Medya Deposu'na öğe başarıyla eklendiyse) devam et.
            uri?.let {
                // resolver.openOutputStream(it) metodu, verilen URI'ye veri yazmak için
                // bir OutputStream açar. 'it' burada 'uri' değişkenini temsil eder.
                // '.use' bloğu, işlem bittiğinde veya bir hata oluştuğunda OutputStream'in
                // otomatik olarak kapatılmasını sağlar (kaynak sızıntılarını önler).
                resolver.openOutputStream(it)?.use { output -> // 'output' burada açılan OutputStream'dir.
                    // bitmap.compress() metodu, bitmap verisini belirtilen formatta (PNG)
                    // ve kalitede (100 - kayıpsız sıkıştırma için PNG'de genellikle göz ardı edilir)
                    // OutputStream'e yazar.
                    bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, output)
                }
            }
        }
    }


    override suspend fun shareStickerDetailImage(
        context: Context,
        bitmap: Bitmap
    ) {
        // bitmap'i cache'e kaydet
        val cachePath = File(context.cacheDir, "shared_sticker")
        cachePath.mkdirs()

        val file = File(cachePath,"sticker.png")

        withContext(Dispatchers.IO) {
            FileOutputStream(file).use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            }
        }
        // FileProvider ile uri oluştur
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        // 3. Share intent
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        // 4. Android’in kendi paylaşım menüsünü aç
        context.startActivity(Intent.createChooser(shareIntent, "Paylaş"))

    }

    override suspend fun getCategoryDownloadUrl(filePath: String): String? {
        return try {
            val storageRef = storage.reference.child(filePath)
            storageRef.downloadUrl.await().toString()
        }catch (e:Exception){
            null
        }
    }


}