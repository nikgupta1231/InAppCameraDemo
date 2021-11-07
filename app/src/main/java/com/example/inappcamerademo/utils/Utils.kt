package com.example.inappcamerademo.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Environment
import androidx.core.content.ContextCompat
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    val df = SimpleDateFormat("MMM dd, yyyy hh:mm:ss aa", Locale.getDefault())
    fun getRotateBitmap(source: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(90f)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    fun getImageThumbnail(imageFile: File, sampleSize: Int): Bitmap {
        val ops = BitmapFactory.Options()
        ops.inSampleSize = sampleSize
        return BitmapFactory.decodeFile(imageFile.path, ops)
    }

    val dir: File
        get() {
            val sdDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            return File(sdDir, "InAppDemoGallery")
        }

    fun isCameraPermissionAvailable(context: Context?): Boolean {
        return ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isWriteExternalPermissionAvailable(context: Context?): Boolean {
        return ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
}