package com.courage.vibestickers.repository

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.courage.vibestickers.data.model.CreatedStickers
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class CreatedImpl(private val firestore: FirebaseFirestore, private val firebaseStorage: FirebaseStorage):CreatedRepository {
    override suspend fun getCreatedStickers(): List<CreatedStickers> {
        return try {
            val snapshot = firestore.collection("createdStickers").get().await()
            snapshot.documents.mapNotNull { it.toObject(CreatedStickers::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }


    override suspend fun getCreatedImages(filePath: String): Bitmap? {
        return try {
            val storageRef = firebaseStorage.reference.child(filePath)
            val byteArray = storageRef.getBytes(5 * 1024 * 1024).await()
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        } catch (e: Exception) {
            null
        }
    }


     override suspend fun getDownloadUrl(filePath: String): String? {

         // firestorede downloadUrl yi döndürür
         return try {
             val storageRef = firebaseStorage.reference.child(filePath)
             storageRef.downloadUrl.await().toString()
         }catch (e:Exception){
             null
         }
     }





    // Bu fonksiyon, verilen bitmap listesini cihazın galerisine kaydeder.
    // 'suspend' anahtar kelimesi, bu fonksiyonun bir korutin içinde çağrılması gerektiğini belirtir,
    // çünkü dosya işlemleri potansiyel olarak zaman alıcı olabilir ve ana thread'i bloke etmemelidir.
    override suspend fun saveStickersToGallery(context: Context, bitmaps: List<Bitmap>) {
        // ContentResolver, uygulamanın diğer uygulamaların verilerine (örneğin Medya Deposu)
        // erişmesini sağlayan bir arayüzdür.
        val resolver = context.contentResolver

        // Kaydedilecek sticker'ların galeri içinde bulunacağı klasörün adı.
        // Bu, genellikle "Pictures" klasörünün altında bir alt klasör olarak oluşturulur.
        val directory = "CreatedMyStickers"

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



    override suspend fun deleteCreatedSticker(stickerId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun addCreatedStickerImage(
        context: Context,
        croppedStickerBitmap: Bitmap?
    ): Result<Unit> {
        return try {
            if (croppedStickerBitmap == null) {
                return Result.failure(IllegalArgumentException("Bitmap is null"))
            }

            // Storage içinde benzersiz bir dosya yolu
            val storageFileName = "${System.currentTimeMillis()}.png"
            val storagePath = "created_stickers/$storageFileName" // KAYDEDİLECEK YOL BU
            val imageRef = firebaseStorage.reference.child(storagePath)

            // Bitmap'i geçici bir dosyaya kaydet
            val file = File(context.cacheDir, "temp_${System.currentTimeMillis()}.png") // Geçici dosya adına da benzersizlik ekleyelim
            val outputStream = withContext(Dispatchers.IO) {
                FileOutputStream(file)
            }
            croppedStickerBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            withContext(Dispatchers.IO) {
                outputStream.flush()
            }
            withContext(Dispatchers.IO) {
                outputStream.close()
            }

            // Dosyayı Storage'a yükle
            imageRef.putFile(Uri.fromFile(file)).await()

            // Geçici dosyayı sil (isteğe bağlı ama iyi bir pratik)
            withContext(Dispatchers.IO) {
                if (file.exists()) {
                    file.delete()
                }
            }


            // Yükleme bitince oluşturulan download URL'sini al (BU SATIRA ARTIK İHTİYAÇ YOK,
            // EĞER SADECE STORAGE PATH'İNİ KULLANACAKSANIZ)
            // val downloadUrl = imageRef.downloadUrl.await().toString()

            // Firestore document referansı
            val docRef = firestore.collection("createdStickers").document()
            val createdId = docRef.id

            // Data class ile nesneyi oluştur
            val newSticker = CreatedStickers(
                createdId = createdId,
                createdImageUrl = storagePath // <-- DEĞİŞİKLİK BURADA: storagePath KULLANILMALI
            )

            // Firestore'a kaydet
            docRef.set(newSticker).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AddStickerError", "Error adding created sticker image", e) // Hata loglaması ekle
            Result.failure(e)
        }
    }

}
