package com.example.inappcamerademo.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.inappcamerademo.CameraView;
import com.example.inappcamerademo.MainDbHelper;
import com.example.inappcamerademo.R;
import com.example.inappcamerademo.Utils;
import com.example.inappcamerademo.model.ImageInfo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class HomeScreenActivity extends AppCompatActivity implements View.OnClickListener, Camera.PictureCallback {

    private static final String TAG = "HomeScreen";

    private CameraView mCameraView;

    private FloatingActionButton picCaptureBtn;
    private ImageView previewImage;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private MainDbHelper mainDbHelper;


    private static final int REQ_CODE_CAMERA = 0x1;
    private static final int REQ_CODE_WRITE_EXTERNAL_STORAGE = 0x2;
    private static final int REQ_CODE_FINE_LOCATION = 0x4;

    private static final String NO_CAMERA_PERMISSION = "Please give camera permission to capture new images!";
    private static final String NO_READ_WRITE_PERMISSION = "Please give Read & Write permission to save and display images!";

    private AlertDialog alertDialogOne, alertDialogTwo, alertDialogThree;

    private void checkPermissions() {
        boolean isCamPerGranted, isWriteExtPerGranted, isForegroundLocPerGranted;

        isCamPerGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        isWriteExtPerGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        isForegroundLocPerGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (!isCamPerGranted && !isWriteExtPerGranted) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) || shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                alertDialogOne = displayNeverAskAgainDialog(NO_CAMERA_PERMISSION + "\n" + NO_READ_WRITE_PERMISSION).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQ_CODE_CAMERA | REQ_CODE_WRITE_EXTERNAL_STORAGE);
            }
        } else if (!isCamPerGranted) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                alertDialogTwo = displayNeverAskAgainDialog(NO_CAMERA_PERMISSION).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQ_CODE_CAMERA);
            }
        } else if (!isWriteExtPerGranted) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                alertDialogThree = displayNeverAskAgainDialog(NO_READ_WRITE_PERMISSION).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQ_CODE_WRITE_EXTERNAL_STORAGE);
            }
        }
        if (!isForegroundLocPerGranted && !shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_CODE_FINE_LOCATION);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPermissions();

        ImageInfo info = mainDbHelper.getLastCapturedImage();
        if (info != null) {
            Glide.with(HomeScreenActivity.this).load(new File(info.getPath())).into(previewImage);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            findViewById(R.id.error_message).setVisibility(View.GONE);
        } else {
            findViewById(R.id.error_message).setVisibility(View.VISIBLE);
        }

        if (alertDialogOne != null && Utils.isCameraPermissionAvailable(this) && Utils.isWriteExternalPermissionAvailable(this)) {
            if (alertDialogOne.isShowing())
                alertDialogOne.cancel();
            alertDialogOne = null;
        }
        if (alertDialogTwo != null && Utils.isCameraPermissionAvailable(this)) {
            if (alertDialogTwo.isShowing())
                alertDialogTwo.cancel();
            alertDialogTwo = null;
        }
        if (alertDialogThree != null && Utils.isWriteExternalPermissionAvailable(this)) {
            if (alertDialogThree.isShowing())
                alertDialogThree.cancel();
            alertDialogThree = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermissions();

        setContentView(R.layout.activity_home_screen);
        mainDbHelper = MainDbHelper.getInstance(this.getApplicationContext());

        //camera preview screen
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        mCameraView = new CameraView(this);
        preview.addView(mCameraView);

        // grab out shutter button so we can reference it later
        picCaptureBtn = (FloatingActionButton) findViewById(R.id.picture_capture_btn);
        picCaptureBtn.setOnClickListener(this);

        // previews the last captured images and on-clicking this we go to all captured images
        previewImage = (ImageView) findViewById(R.id.image_preview_btn);
        previewImage.setOnClickListener(this);

        //fused location client for getting current location in battery optimised way.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.picture_capture_btn:
                takePicture();
                break;
            case R.id.image_preview_btn:
                Intent intent = new Intent(getApplicationContext(), ImagesGridViewActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void takePicture() {
        picCaptureBtn.setEnabled(false);
        mCameraView.takePicture(this);
    }

    @Override
    public void onPictureTaken(final byte[] data, Camera camera) {

        File pictureFileDir = Utils.getDir();
        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
            Log.d(TAG, "Can't create directory to save image.");
            return;
        }

        boolean isLocationAvailable = false;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission not available, not storing location.");
        } else {
            isLocationAvailable = true;
            mFusedLocationProviderClient.requestLocationUpdates(getLocationRequest(), mLocationCallback, Looper.myLooper());
        }

        pictureCaptureTs = System.currentTimeMillis() / 1000;
        pictureFileName = "Picture_" + pictureCaptureTs + ".jpg";
        pictureFilePath = pictureFileDir.getPath() + File.separator + pictureFileName;
        final File pictureFile = new File(pictureFilePath);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream inputStream = new ByteArrayInputStream(data);
                    Bitmap originalImage = BitmapFactory.decodeStream(inputStream);
                    Bitmap rotatedImage = Utils.getRotateBitmap(originalImage);

                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    rotatedImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            previewImage.setImageBitmap(Utils.getImageThumbnail(pictureFile, 64));
                        }
                    });
                } catch (Exception error) {
                    Log.d(TAG, "File" + pictureFileName + "not saved: " + error.getMessage());
                }
            }
        }).start();

        if (!isLocationAvailable) {
            ImageInfo imageInfo = new ImageInfo();
            imageInfo.setName(pictureFileName);
            imageInfo.setPath(pictureFilePath);
            imageInfo.setTimestamp(pictureCaptureTs);
            mainDbHelper.addImage(imageInfo);
        }

        // Restart the preview and re-enable the shutter button so that we can take another picture
        camera.startPreview();
        picCaptureBtn.setEnabled(true);
    }

    private String pictureFileName, pictureFilePath;
    private long pictureCaptureTs;

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location lastLocation = locationResult.getLastLocation();

            ImageInfo imageInfo = new ImageInfo();
            if (lastLocation != null) {
                imageInfo.setLat(lastLocation.getLatitude());
                imageInfo.setLon(lastLocation.getLongitude());
            }

            imageInfo.setName(pictureFileName);
            imageInfo.setPath(pictureFilePath);
            imageInfo.setTimestamp(pictureCaptureTs);

            mainDbHelper.addImage(imageInfo);
        }
    };

    private LocationRequest mLocationRequest;

    private LocationRequest getLocationRequest() {
        if (mLocationRequest == null) {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(0);
            mLocationRequest.setFastestInterval(0);
            mLocationRequest.setNumUpdates(1);
        }
        return mLocationRequest;
    }

    private AlertDialog.Builder displayNeverAskAgainDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message + "\nPlease permit the permission through "
                + "Settings screen.\n\nSelect Permissions -> Enable permission");
        builder.setCancelable(false);
        builder.setPositiveButton("Permit Manually", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", null);
        return builder;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ((requestCode & REQ_CODE_CAMERA) == REQ_CODE_CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            mCameraView = new CameraView(this);
            preview.addView(mCameraView);
        }
    }
}
