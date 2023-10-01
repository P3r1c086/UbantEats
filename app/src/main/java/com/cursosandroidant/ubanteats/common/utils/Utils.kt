package com.cursosandroidant.ubanteats.common.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap

object Utils {
    //funcion para convertir una imagen de vector asset drawable en bitmap
    fun getBitmapFromVector(context: Context, resId: Int): Bitmap?{
        return AppCompatResources.getDrawable(context, resId)?.toBitmap()
    }
}