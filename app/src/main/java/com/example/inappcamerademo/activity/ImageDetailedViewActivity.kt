package com.example.inappcamerademo.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.inappcamerademo.MainDbHelper
import com.example.inappcamerademo.R
import com.example.inappcamerademo.model.ImageInfo
import com.example.inappcamerademo.utils.Utils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileInputStream

class ImageDetailedViewActivity : AppCompatActivity(), OnMapReadyCallback {
    private var imageInfo: ImageInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_image_details)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.image_map_location) as SupportMapFragment?

        mapFragment?.let {
            it.getMapAsync(this)
        }

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setTitle("Information")

        val mainDbHelper = MainDbHelper.getInstance(this.applicationContext)

        intent.getStringExtra("name")?.also {
            imageInfo = mainDbHelper?.getImageInfoFromImageName(it)
        }

        imageInfo?.path?.let {
            val imageFile = File(it)
            val bitmap: Bitmap
            try {
                bitmap = BitmapFactory.decodeStream(FileInputStream(imageFile))
                (findViewById<View>(R.id.captured_image) as ImageView).setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        (findViewById<View>(R.id.image_name) as TextView).text =
            String.format("File name: %s", imageInfo?.name)
        (findViewById<View>(R.id.image_time) as TextView).text =
            String.format("Time: %s", Utils.df.format(imageInfo?.timestamp?.times(1000)))
        (findViewById<View>(R.id.image_path) as TextView).text =
            String.format("File path: %s", imageInfo?.path)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        // Add a marker in Sydney and move the camera
        if (imageInfo!!.lat != 0.0 && imageInfo!!.lat != 0.0) {
            val latLng = LatLng(imageInfo!!.lat, imageInfo!!.lon)
            googleMap.addMarker(MarkerOptions().position(latLng))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            googleMap.setMinZoomPreference(15f)
        }
    }

}