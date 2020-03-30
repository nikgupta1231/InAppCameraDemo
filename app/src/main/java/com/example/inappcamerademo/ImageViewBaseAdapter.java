package com.example.inappcamerademo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.inappcamerademo.model.ImageInfo;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class ImageViewBaseAdapter extends BaseAdapter {

    private List<String> imageFileList;
    private Context context;
    private Activity activity;

    public ImageViewBaseAdapter(Context context, Activity activity, List<String> imageFileNames) {
        this.context = context;
        this.activity = activity;
        this.imageFileList = imageFileNames;
    }

    @Override
    public int getCount() {
        return imageFileList.size();
    }

    @Override
    public Object getItem(int i) {
        return imageFileList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.captured_image_view, viewGroup, false);
        }
        try {
            final ImageView imageView = view.findViewById(R.id.image);
            MainDbHelper mainDbHelper = MainDbHelper.getInstance(context);

            final ImageInfo imageInfo = mainDbHelper.getImageInfoFromImageName(imageFileList.get(i));

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = null;
                    try {
                        BitmapFactory.Options ops = new BitmapFactory.Options();
                        ops.inSampleSize = 16;
                        bitmap = BitmapFactory.decodeStream(new FileInputStream(new File(imageInfo.getPath())), null, ops);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final Bitmap finalBitmap = bitmap;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(finalBitmap);
                        }
                    });
                }
            }).start();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

}
