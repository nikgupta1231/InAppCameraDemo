package com.example.inappcamerademo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.inappcamerademo.model.ImageInfo;

import java.util.ArrayList;
import java.util.List;

public class MainDbHelper extends SQLiteOpenHelper {

    private static final String TAG = "MainDbHelper";

    private static final String DATABASE_NAME = "ImageInfoDb";
    private static final int DATABASE_VERSION = 1;

    private static MainDbHelper mInstance;

    private static final String TABLE_NAME = "IMAGE_DATA";
    private static final String KEY_IMAGE_NAME = "NAME";
    private static final String KEY_IMAGE_LAT = "LAT";
    private static final String KEY_IMAGE_LON = "LON";
    private static final String KEY_IMAGE_TIME = "TIME";
    private static final String KEY_IMAGE_PATH = "PATH";


    private static final String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + KEY_IMAGE_NAME + " TEXT PRIMARY KEY,"
            + KEY_IMAGE_LAT + " DOUBLE,"
            + KEY_IMAGE_LON + " DOUBLE,"
            + KEY_IMAGE_TIME + " DOUBLE,"
            + KEY_IMAGE_PATH + " TEXT"
            + ")";


    private MainDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static MainDbHelper getInstance(Context context) {
        if (mInstance == null)
            mInstance = new MainDbHelper(context);
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addImage(ImageInfo imageInfo) {
        Log.v(TAG, "addImage:" + imageInfo);

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IMAGE_NAME, imageInfo.getName());
        values.put(KEY_IMAGE_TIME, imageInfo.getTimestamp());
        values.put(KEY_IMAGE_LAT, imageInfo.getLat());
        values.put(KEY_IMAGE_LON, imageInfo.getLon());
        values.put(KEY_IMAGE_PATH, imageInfo.getPath());

        long id = db.insert(TABLE_NAME, null, values);
        Log.v(TAG, "id:" + id);
        db.close();
    }

    public ImageInfo getImageInfoFromImageName(String name) {
        Log.v(TAG, "name:" + name);

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_IMAGE_NAME + " =?";
        Cursor cursor = db.rawQuery(query, new String[]{name});
        ImageInfo imageInfo = null;
        if (cursor != null && cursor.moveToFirst()) {
            imageInfo = new ImageInfo();
            imageInfo.setName(cursor.getString(cursor.getColumnIndex(KEY_IMAGE_NAME)));
            imageInfo.setTimestamp(cursor.getLong(cursor.getColumnIndex(KEY_IMAGE_TIME)));
            imageInfo.setLat(cursor.getDouble(cursor.getColumnIndex(KEY_IMAGE_LAT)));
            imageInfo.setLon(cursor.getDouble(cursor.getColumnIndex(KEY_IMAGE_LON)));
            imageInfo.setPath(cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH)));
            cursor.close();
        }
        db.close();
        return imageInfo;
    }

    public List<String> getAllImageNames() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + KEY_IMAGE_NAME + " FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        List<String> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }

    public List<String> getAllImageNamesSortedByName() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + KEY_IMAGE_NAME + " FROM " + TABLE_NAME + " ORDER BY " + KEY_IMAGE_NAME + " ASC ";
        Cursor cursor = db.rawQuery(query, null);
        List<String> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }

    public List<String> getAllImageNamesSortedByTime() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + KEY_IMAGE_NAME + " FROM " + TABLE_NAME + " ORDER BY " + KEY_IMAGE_TIME + " DESC ";
        Cursor cursor = db.rawQuery(query, null);
        List<String> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }

    public ImageInfo getLastCapturedImage() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + KEY_IMAGE_TIME + " DESC ";
        Cursor cursor = db.rawQuery(query, null);

        ImageInfo imageInfo = null;
        if (cursor.moveToFirst()) {
            imageInfo = new ImageInfo();
            imageInfo.setName(cursor.getString(cursor.getColumnIndex(KEY_IMAGE_NAME)));
            imageInfo.setTimestamp(cursor.getLong(cursor.getColumnIndex(KEY_IMAGE_TIME)));
            imageInfo.setLat(cursor.getDouble(cursor.getColumnIndex(KEY_IMAGE_LAT)));
            imageInfo.setLon(cursor.getDouble(cursor.getColumnIndex(KEY_IMAGE_LON)));
            imageInfo.setPath(cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH)));
        }

        cursor.close();
        db.close();

        return imageInfo;
    }

}
