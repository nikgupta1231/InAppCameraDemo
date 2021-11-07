package com.example.inappcamerademo.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.inappcamerademo.HomeScreenViewModel
import com.example.inappcamerademo.R
import com.example.inappcamerademo.databinding.ActivityHomeScreenBinding
import com.example.inappcamerademo.model.ImageInfo
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import dagger.hilt.android.AndroidEntryPoint

class HomeScreenActivity : AppCompatActivity() {

    companion object {

        private const val TAG = "HomeScreen"
        private const val REQ_CODE_CAMERA = 0x1
        private const val REQ_CODE_WRITE_EXTERNAL_STORAGE = 0x2
        private const val REQ_CODE_FINE_LOCATION = 0x4
        private const val NO_CAMERA_PERMISSION =
            "Please give camera permission to capture new images!"
        private const val NO_READ_WRITE_PERMISSION =
            "Please give Read & Write permission to save and display images!"

    }

    lateinit var viewModel: HomeScreenViewModel
    lateinit var binding: ActivityHomeScreenBinding

//    @RequiresApi(Build.VERSION_CODES.M)
//    private fun checkPermissions() {
//        val isCamPerGranted: Boolean = ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.CAMERA
//        ) == PackageManager.PERMISSION_GRANTED
//        val isWriteExtPerGranted: Boolean = ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//        ) == PackageManager.PERMISSION_GRANTED
//        val isForegroundLocPerGranted: Boolean = ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED
//        if (!isCamPerGranted && !isWriteExtPerGranted) {
//            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) || shouldShowRequestPermissionRationale(
//                    Manifest.permission.CAMERA
//                )
//            ) {
//                alertDialogOne =
//                    displayNeverAskAgainDialog("$NO_CAMERA_PERMISSION \n $NO_READ_WRITE_PERMISSION").show()
//            } else {
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
//                    REQ_CODE_CAMERA or REQ_CODE_WRITE_EXTERNAL_STORAGE
//                )
//            }
//        } else if (!isCamPerGranted) {
//            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
//                alertDialogTwo = displayNeverAskAgainDialog(NO_CAMERA_PERMISSION).show()
//            } else {
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(Manifest.permission.CAMERA),
//                    REQ_CODE_CAMERA
//                )
//            }
//        } else if (!isWriteExtPerGranted) {
//            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                alertDialogThree = displayNeverAskAgainDialog(NO_READ_WRITE_PERMISSION).show()
//            } else {
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                    REQ_CODE_WRITE_EXTERNAL_STORAGE
//                )
//            }
//        }
//        if (!isForegroundLocPerGranted && !shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                REQ_CODE_FINE_LOCATION
//            )
//        }
//    }

//    override fun onResume() {
//        super.onResume()
//        checkPermissions()
//        val info = mainDbHelper!!.lastCapturedImage
//        if (info != null) {
//            Glide.with(this@HomeScreenActivity).load(File(info.path)).into(previewImage!!)
//        }
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.CAMERA
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            findViewById<View>(R.id.error_message).visibility = View.GONE
//        } else {
//            findViewById<View>(R.id.error_message).visibility = View.VISIBLE
//        }
//        if (alertDialogOne != null && Utils.isCameraPermissionAvailable(this) && Utils.isWriteExternalPermissionAvailable(
//                this
//            )
//        ) {
//            if (alertDialogOne!!.isShowing) alertDialogOne!!.cancel()
//            alertDialogOne = null
//        }
//        if (alertDialogTwo != null && Utils.isCameraPermissionAvailable(this)) {
//            if (alertDialogTwo!!.isShowing) alertDialogTwo!!.cancel()
//            alertDialogTwo = null
//        }
//        if (alertDialogThree != null && Utils.isWriteExternalPermissionAvailable(this)) {
//            if (alertDialogThree!!.isShowing) alertDialogThree!!.cancel()
//            alertDialogThree = null
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen)
        viewModel = ViewModelProvider(this).get(HomeScreenViewModel::class.java)

//        checkPermissions()

        //camera preview screen
//        val preview = findViewById<View>(R.id.camera_preview) as FrameLayout
//        mCameraView = CameraView(this)
//        preview.addView(mCameraView)


        //fused location client for getting current location in battery optimised way.
//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

//    override fun onClick(v: View) {
//        when (v.id) {
//            R.id.picture_capture_btn -> takePicture()
//            R.id.image_preview_btn -> {
//                val intent = Intent(applicationContext, ImagesGridViewActivity::class.java)
//                startActivity(intent)
//            }
//        }
//    }

    private fun takePicture() {
//        picCaptureBtn!!.isEnabled = false
//        mCameraView!!.takePicture(this)
    }

//    override fun onPictureTaken(data: ByteArray, camera: Camera) {
//        val pictureFileDir = Utils.dir
//        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
//            Log.d(TAG, "Can't create directory to save image.")
//            return
//        }
//        var isLocationAvailable = false
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            Log.d(TAG, "Permission not available, not storing location.")
//        } else {
//            isLocationAvailable = true
//            mFusedLocationProviderClient!!.requestLocationUpdates(
//                locationRequest,
//                mLocationCallback,
//                Looper.myLooper()
//            )
//        }
//        pictureCaptureTs = System.currentTimeMillis() / 1000
//        pictureFileName = "Picture_$pictureCaptureTs.jpg"
//        pictureFilePath = pictureFileDir.path + File.separator + pictureFileName
//        val pictureFile = File(pictureFilePath)
//        Thread {
//            try {
//                val inputStream: InputStream = ByteArrayInputStream(data)
//                val originalImage = BitmapFactory.decodeStream(inputStream)
//                val rotatedImage = Utils.getRotateBitmap(originalImage)
//                val fos = FileOutputStream(pictureFile)
//                rotatedImage.compress(Bitmap.CompressFormat.JPEG, 100, fos)
//                runOnUiThread {
//                    previewImage!!.setImageBitmap(
//                        Utils.getImageThumbnail(
//                            pictureFile,
//                            64
//                        )
//                    )
//                }
//            } catch (error: Exception) {
//                Log.d(TAG, "File" + pictureFileName + "not saved: " + error.message)
//            }
//        }.start()
//        if (!isLocationAvailable) {
//            val imageInfo = ImageInfo(
//                pictureFileName!!,
//                pictureCaptureTs,
//                pictureFilePath!!
//            )
//            mainDbHelper!!.addImage(imageInfo)
//        }
//
//        // Restart the preview and re-enable the shutter button so that we can take another picture
//        camera.startPreview()
//        picCaptureBtn!!.isEnabled = true
//    }

    private var pictureFileName: String? = null
    private var pictureFilePath: String? = null
    private var pictureCaptureTs: Long = 0
    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation = locationResult.lastLocation
            val imageInfo = ImageInfo(
                pictureFileName!!,
                pictureCaptureTs,
                pictureFilePath!!,
                lastLocation.latitude,
                lastLocation.longitude
            )
//            mainDbHelper!!.addImage(imageInfo)
        }
    }
    private var mLocationRequest: LocationRequest? = null
    private val locationRequest: LocationRequest
        private get() {
            if (mLocationRequest == null) {
                mLocationRequest = LocationRequest()
                mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                mLocationRequest!!.interval = 0
                mLocationRequest!!.fastestInterval = 0
                mLocationRequest!!.numUpdates = 1
            }
            return mLocationRequest!!
        }

    private fun displayNeverAskAgainDialog(message: String): AlertDialog.Builder {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("$message Please permit the permission through Settings screen. Select Permissions -> Enable permission")
        builder.setCancelable(false)
        builder.setPositiveButton("Permit Manually") { dialog, which ->
            dialog.dismiss()
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        builder.setNegativeButton("Cancel", null)
        return builder
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode and REQ_CODE_CAMERA == REQ_CODE_CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            val preview = findViewById<View>(R.id.camera_preview) as FrameLayout
//            mCameraView = CameraView(this)
//            preview.addView(mCameraView)
        }
    }


}