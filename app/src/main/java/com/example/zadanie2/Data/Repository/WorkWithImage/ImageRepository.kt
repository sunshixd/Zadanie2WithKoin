package com.example.zadanie2.Data.Repository.WorkWithImage

import android.content.Context
import android.graphics.Bitmap

interface ImageRepository {
    suspend fun saveImageToGallery(context: Context, imageUrl: String)
}