package com.example.inappcamerademo;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

    private Camera camera;

    public CameraView(Context context) {
        super(context);
        if (getHolder() != null) {
            getHolder().addCallback(this);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        try {
            if (camera != null) {
                camera.setDisplayOrientation(90);
                camera.startPreview();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = Camera.open();
            camera.setPreviewDisplay(holder);
        } catch (IOException ioe) {
            ioe.printStackTrace(System.out);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            if (camera != null) {
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // do nothing
    }

    public void takePicture(PictureCallback imageCallback) {
        try {
            if (camera != null) {
                camera.takePicture(null, null, imageCallback);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}