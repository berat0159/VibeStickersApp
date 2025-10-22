package com.courage.vibestickers.view.createscreen

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import coil3.compose.rememberAsyncImagePainter
import androidx.core.graphics.createBitmap

private const val TAG = "CropScreenDebug"

@SuppressLint("MutableCollectionMutableState")
@Composable
fun CropScreen(
    selectedImageUri: Uri?,
    onCropFinished: (Bitmap) -> Unit,
) {
    val context = LocalContext.current

    val paths = remember { mutableStateListOf<androidx.compose.ui.graphics.Path>() }
    var currentPath by remember { mutableStateOf<androidx.compose.ui.graphics.Path?>(null) }
    var currentPoints by remember { mutableStateOf(mutableListOf<Offset>()) }

    // Canvas'ın (ve dolayısıyla üzerinde çizim yapılan Image'ın) boyutunu saklamak için state
    var imageSize by remember { mutableStateOf(IntSize.Zero) }





    Box(modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1f)
        .onGloballyPositioned { coordinates -> // <--- BU SATIRI EKLEYİN
            imageSize = coordinates.size
            Log.d(TAG, "onGloballyPositioned - Box size: ${coordinates.size}")
        }
    ) {

        // Arka plan resmi
        if (selectedImageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(selectedImageUri),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Kullanıcı çizim yapıyor
        Canvas(
            modifier = Modifier
                .matchParentSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            val newPath = androidx.compose.ui.graphics.Path()
                                .apply { moveTo(offset.x, offset.y) }
                            currentPath = newPath
                            currentPoints = mutableListOf(offset)
                        },
                        onDrag = { change, dragAmount ->
                            Log.d(
                                TAG,
                                "onDrag: position=${change.position}, dragAmount=$dragAmount"
                            )

                            // Mevcut yolu kopyalayıp yeni nokta ekleyerek yeni bir Path nesnesi oluştur
                            val updatedPath = currentPath?.let { oldPath ->
                                androidx.compose.ui.graphics.Path().apply {
                                    addPath(oldPath) // Eski yolu kopyala
                                    lineTo(
                                        change.position.x,
                                        change.position.y
                                    ) // Yeni noktayı ekle
                                }
                            } ?: androidx.compose.ui.graphics.Path()
                                .apply { // Eğer currentPath null ise (ilk nokta için pek olası değil ama güvenli)
                                    moveTo(change.position.x, change.position.y)
                                }

                            currentPath = updatedPath // Referansı değiştirerek state'i güncelle
                            currentPoints.add(change.position) // Bu hala otomatik kapama için gerekli
                            Log.d(
                                TAG,
                                "onDrag: currentPath REPLACED. Current points count: ${currentPoints.size}"
                            )
                        },
                        onDragEnd = {
                            Log.d(TAG, "onDragEnd triggered.")
                            currentPath?.let { p ->
                                if (currentPoints.size > 2) {
                                    p.lineTo(currentPoints.first().x, currentPoints.first().y)
                                }
                                paths.add(p)
                            }
                            currentPoints.clear()

                            selectedImageUri?.let { uri ->
                                if (imageSize.width == 0 || imageSize.height == 0) {
                                    Log.e(TAG, "Image size is zero, cannot crop.")
                                    return@let
                                }

                                val sourceBitmap =
                                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                                Log.d(TAG, "Source Bitmap size: ${sourceBitmap.width}x${sourceBitmap.height}")
                                Log.d(TAG, "Canvas (Image View) size: ${imageSize.width}x${imageSize.height}")

                                val maskBitmap =
                                    createBitmap(sourceBitmap.width, sourceBitmap.height, Bitmap.Config.ARGB_8888)
                                val maskCanvas = android.graphics.Canvas(maskBitmap)
                                val maskPaint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply {
                                    color = android.graphics.Color.BLACK // Maske için opak renk
                                    style = android.graphics.Paint.Style.FILL
                                }

                                val combinedAndroidPath = android.graphics.Path()

                                // ContentScale.Crop için dönüşüm matrisini hesapla
                                val transformMatrix = android.graphics.Matrix()

                                val viewWidth = imageSize.width.toFloat()
                                val viewHeight = imageSize.height.toFloat()
                                val bitmapWidth = sourceBitmap.width.toFloat()
                                val bitmapHeight = sourceBitmap.height.toFloat()

                                val bitmapAspectRatio = bitmapWidth / bitmapHeight
                                val viewAspectRatio = viewWidth / viewHeight

                                var scale: Float
                                var dx = 0f
                                var dy = 0f

                                if (bitmapAspectRatio > viewAspectRatio) {
                                    // Bitmap, view'dan daha geniş (veya eşit en/boy oranına sahipse ve daha genişse)
                                    // Yüksekliğe sığdır, genişlikte kırpma veya öteleme olacak
                                    scale = viewHeight / bitmapHeight
                                    dx = (viewWidth - bitmapWidth * scale) * 0.5f // Ortalamak için
                                } else {
                                    // Bitmap, view'dan daha yüksek (veya eşit en/boy oranına sahipse ve daha yüksekse)
                                    // Genişliğe sığdır, yükseklikte kırpma veya öteleme olacak
                                    scale = viewWidth / bitmapWidth
                                    dy = (viewHeight - bitmapHeight * scale) * 0.5f // Ortalamak için
                                }
                                Log.d(TAG, "ContentScale.Crop calculations: scale=$scale, dx=$dx, dy=$dy")

                                // Dönüşüm:
                                // 1. Canvas koordinatlarını, Image composable'ı içindeki görünür bitmap'in
                                //    sol üst köşesine göre ötele (dx, dy çıkarılır).
                                // 2. Sonra bu koordinatları, orijinal bitmap boyutuna ölçekle (1/scale ile çarpılır).
                                transformMatrix.postTranslate(-dx, -dy)
                                transformMatrix.postScale(1 / scale, 1 / scale)

                                paths.forEach { composePath ->
                                    val androidPathSegment = composePath.asAndroidPath()
                                    val segmentBoundsBefore = android.graphics.RectF()
                                    androidPathSegment.computeBounds(segmentBoundsBefore, true)
                                    Log.d(TAG, "Path segment bounds BEFORE transform: $segmentBoundsBefore")

                                    androidPathSegment.transform(transformMatrix)

                                    val segmentBoundsAfter = android.graphics.RectF()
                                    androidPathSegment.computeBounds(segmentBoundsAfter, true)
                                    Log.d(TAG, "Path segment bounds AFTER transform: $segmentBoundsAfter, isEmpty: ${androidPathSegment.isEmpty}")

                                    combinedAndroidPath.op(androidPathSegment, android.graphics.Path.Op.UNION)
                                }

                                val combinedBounds = android.graphics.RectF()
                                combinedAndroidPath.computeBounds(combinedBounds, true)
                                Log.d(TAG, "Combined AndroidPath bounds: $combinedBounds, isEmpty: ${combinedAndroidPath.isEmpty}")

                                if (combinedAndroidPath.isEmpty) {
                                    Log.e(TAG, "Combined path is empty! No mask will be drawn.")
                                    // Belki burada onCropFinished'e orijinal bitmap'i veya bir hata durumu iletmek gerekir.
                                }
                                maskCanvas.drawPath(combinedAndroidPath, maskPaint)


                                val result = createBitmap(sourceBitmap.width, sourceBitmap.height, Bitmap.Config.ARGB_8888)

                                val resultCanvas = android.graphics.Canvas(result)

                                // Önce maskeyi çiz
                                resultCanvas.drawBitmap(maskBitmap, 0f, 0f, null)

                                // Sonra bitmap'i maskeyle uygula
                                val paintXfer = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply {
                                    xfermode = android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN)
                                }
                                resultCanvas.drawBitmap(sourceBitmap, 0f, 0f, paintXfer)


                                Log.d(TAG, "onDragEnd: Bitmap processing finished.")
                                onCropFinished(result)
                            }
                        }
                    )
                }
        ) {
            // Önizleme için pathleri çiz
            paths.forEach { p ->
                drawPath(p, color = Color.Red.copy(alpha = 0.5f), style = Stroke(width = 8f))
            }
            currentPath?.let { p ->
                Log.d("Cizim", "Canvas onDraw: Drawing currentPath.")
                drawPath(p, color = Color.Red.copy(alpha = 0.5f), style = Stroke(width = 15f))

            }
        }
    }
}




