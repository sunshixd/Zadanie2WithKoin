package com.example.zadanie2.Data.Repository.WorkWithImage

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.core.graphics.drawable.toBitmap

class ImageRepositoryImpl : ImageRepository {
    override suspend fun saveImageToGallery(context: Context, imageUrl: String) {
        withContext(Dispatchers.IO) {
            try {
                val bitmap = loadImage(context, imageUrl)
                saveBitmapToGallery(context, bitmap)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Image saved to gallery", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error saving image: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private suspend fun loadImage(context: Context, imageUrl: String): Bitmap {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .allowHardware(false)
            .build()
        val result = (loader.execute(request) as SuccessResult).drawable
        return (result as Drawable).toBitmap()
    }

    private fun saveBitmapToGallery(context: Context, bitmap: Bitmap) {
        val filename = "pokemon_${System.currentTimeMillis()}.png"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/PokemonApp")
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            ?: throw Exception("Failed to create MediaStore entry")

        resolver.openOutputStream(uri)?.use { stream ->
            if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)) {
                throw Exception("Failed to save bitmap")
            }
        } ?: throw Exception("Failed to open output stream")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.clear()
            contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
            resolver.update(uri, contentValues, null, null)
        }

        MediaScannerConnection.scanFile(
            context,
            arrayOf(uri.toString()),
            arrayOf("image/png"),
            null
        )
    }
}