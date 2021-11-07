package com.example.inappcamerademo

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import java.io.File
import java.io.FileInputStream

class ImageViewBaseAdapter(
    private val context: Context,
    private val activity: Activity,
    private val imageFileList: List<String>
) : BaseAdapter() {
    override fun getCount(): Int {
        return imageFileList.size
    }

    override fun getItem(i: Int): Any {
        return imageFileList[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, view: View, viewGroup: ViewGroup): View {
        var view = view
        if (view == null) {
            view =
                LayoutInflater.from(context).inflate(R.layout.captured_image_view, viewGroup, false)
        }
        try {
            val imageView = view.findViewById<ImageView>(R.id.image)
            val mainDbHelper = MainDbHelper.getInstance(context)
            val imageInfo = mainDbHelper?.getImageInfoFromImageName(imageFileList[i])
            Thread {
                var bitmap: Bitmap? = null
                try {
                    val ops = BitmapFactory.Options()
                    ops.inSampleSize = 16
                    bitmap = BitmapFactory.decodeStream(
                        FileInputStream(File(imageInfo?.path)),
                        null,
                        ops
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                val finalBitmap = bitmap
                activity.runOnUiThread { imageView.setImageBitmap(finalBitmap) }
            }.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return view
    }
}