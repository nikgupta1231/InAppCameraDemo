package com.example.inappcamerademo

import android.content.Context
import android.hardware.Camera
import android.hardware.Camera.PictureCallback
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.io.IOException

class CameraView(context: Context?) : SurfaceView(context), SurfaceHolder.Callback {
    private var camera: Camera? = null
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        try {
            if (camera != null) {
                camera!!.setDisplayOrientation(90)
                camera!!.startPreview()
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        try {
            camera = Camera.open().apply {
                setPreviewDisplay(holder)
            }
        } catch (ioe: IOException) {
            ioe.printStackTrace(System.out)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        camera?.let {
            it.stopPreview()
            it.release()
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // do nothing
    }

    fun takePicture(imageCallback: PictureCallback?) {
        try {
            if (camera != null) {
                camera!!.takePicture(null, null, imageCallback)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    init {
        if (holder != null) {
            holder.addCallback(this)
        }
    }
}