package com.example.inappcamerademo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Utils {

    public static final SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aa", Locale.getDefault());

    public static Bitmap getRotateBitmap(Bitmap source) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static Bitmap getImageThumbnail(File imageFile, int sampleSize) {
        BitmapFactory.Options ops = new BitmapFactory.Options();
        ops.inSampleSize = sampleSize;
        return BitmapFactory.decodeFile(imageFile.getPath(), ops);
    }

    public static File getDir() {
        File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(sdDir, "InAppDemoGallery");
    }

    public static boolean isCameraPermissionAvailable(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isWriteExternalPermissionAvailable(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

}

