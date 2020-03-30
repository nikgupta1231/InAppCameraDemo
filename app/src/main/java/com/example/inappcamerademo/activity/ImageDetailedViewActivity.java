package com.example.inappcamerademo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.inappcamerademo.MainDbHelper;
import com.example.inappcamerademo.R;
import com.example.inappcamerademo.Utils;
import com.example.inappcamerademo.model.ImageInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileInputStream;

public class ImageDetailedViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ImageInfo imageInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.image_map_location);
        mapFragment.getMapAsync(this);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Information");

        Intent intent = getIntent();

        String fileName = intent.getStringExtra("name");
        MainDbHelper mainDbHelper = MainDbHelper.getInstance(this.getApplicationContext());

        imageInfo = mainDbHelper.getImageInfoFromImageName(fileName);

        File imageFile = new File(imageInfo.getPath());
        Bitmap bitmap;

        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(imageFile));
            ((ImageView) findViewById(R.id.captured_image)).setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ((TextView) findViewById(R.id.image_name)).setText(String.format("File name: %s", imageInfo.getName()));
        ((TextView) findViewById(R.id.image_time)).setText(String.format("Time: %s", Utils.df.format(imageInfo.getTimestamp() * 1000)));
        ((TextView) findViewById(R.id.image_path)).setText(String.format("File path: %s", imageInfo.getPath()));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add a marker in Sydney and move the camera
        if (imageInfo.getLat() != 0 && imageInfo.getLat() != 0) {
            LatLng latLng = new LatLng(imageInfo.getLat(), imageInfo.getLon());
            googleMap.addMarker(new MarkerOptions().position(latLng));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.setMinZoomPreference(15);
        }
    }
}
