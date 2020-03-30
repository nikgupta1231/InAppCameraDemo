package com.example.inappcamerademo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.inappcamerademo.ImageViewBaseAdapter;
import com.example.inappcamerademo.MainDbHelper;
import com.example.inappcamerademo.R;

import java.util.List;

public class ImagesGridViewActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "ImagesGridViewActivity";

    private ImageViewBaseAdapter viewBaseAdapter;
    private List<String> imageFileNameList;
    private MainDbHelper mainDbHelper;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Images");

        GridView gridView = findViewById(R.id.images_grid_view);
        mainDbHelper = MainDbHelper.getInstance(this.getApplicationContext());

        this.imageFileNameList = mainDbHelper.getAllImageNames();
        viewBaseAdapter = new ImageViewBaseAdapter(this.getApplicationContext(), (Activity) ImagesGridViewActivity.this, this.imageFileNameList);
        gridView.setAdapter(viewBaseAdapter);
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, ImageDetailedViewActivity.class);
        intent.putExtra("name", imageFileNameList.get(i));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_images_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_by_name:
                Log.v(TAG, "Sorted by name");
                this.imageFileNameList.clear();
                this.imageFileNameList.addAll(mainDbHelper.getAllImageNamesSortedByName());
                viewBaseAdapter.notifyDataSetChanged();
                break;
            case R.id.action_sort_by_time:
                Log.v(TAG, "Sorted by time");
                this.imageFileNameList.clear();
                this.imageFileNameList.addAll(mainDbHelper.getAllImageNamesSortedByTime());
                viewBaseAdapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
