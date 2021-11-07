package com.example.inappcamerademo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.inappcamerademo.model.ImageInfo
import java.util.*

class MainDbHelper private constructor(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(sqLiteDatabase)
    }

    fun addImage(imageInfo: ImageInfo) {
        Log.v(TAG, "addImage:$imageInfo")
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_IMAGE_NAME, imageInfo.name)
        values.put(KEY_IMAGE_TIME, imageInfo.timestamp)
        values.put(KEY_IMAGE_LAT, imageInfo.lat)
        values.put(KEY_IMAGE_LON, imageInfo.lon)
        values.put(KEY_IMAGE_PATH, imageInfo.path)
        val id = db.insert(TABLE_NAME, null, values)
        Log.v(TAG, "id:$id")
        db.close()
    }

    fun getImageInfoFromImageName(name: String): ImageInfo? {
        Log.v(TAG, "name:$name")
        val db = this.readableDatabase
        val query = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_IMAGE_NAME + " =?"
        val cursor = db.rawQuery(query, arrayOf(name))
        var imageInfo: ImageInfo? = null
        if (cursor != null && cursor.moveToFirst()) {
            imageInfo = ImageInfo(
                cursor.getString(cursor.getColumnIndex(KEY_IMAGE_NAME)),
                cursor.getLong(cursor.getColumnIndex(KEY_IMAGE_TIME)),
                cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH)),
                cursor.getDouble(cursor.getColumnIndex(KEY_IMAGE_LAT)),
                cursor.getDouble(cursor.getColumnIndex(KEY_IMAGE_LON))
            )
            cursor.close()
        }
        db.close()
        return imageInfo
    }

    val allImageNames: List<String>
        get() {
            val db = this.readableDatabase
            val query = "SELECT " + KEY_IMAGE_NAME + " FROM " + TABLE_NAME
            val cursor = db.rawQuery(query, null)
            val list: MutableList<String> = ArrayList()
            if (cursor.moveToFirst()) {
                do {
                    list.add(cursor.getString(0))
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return list
        }
    val allImageNamesSortedByName: List<String>
        get() {
            val db = this.readableDatabase
            val query =
                "SELECT " + KEY_IMAGE_NAME + " FROM " + TABLE_NAME + " ORDER BY " + KEY_IMAGE_NAME + " ASC "
            val cursor = db.rawQuery(query, null)
            val list: MutableList<String> = ArrayList()
            if (cursor.moveToFirst()) {
                do {
                    list.add(cursor.getString(0))
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return list
        }
    val allImageNamesSortedByTime: List<String>
        get() {
            val db = this.readableDatabase
            val query =
                "SELECT " + KEY_IMAGE_NAME + " FROM " + TABLE_NAME + " ORDER BY " + KEY_IMAGE_TIME + " DESC "
            val cursor = db.rawQuery(query, null)
            val list: MutableList<String> = ArrayList()
            if (cursor.moveToFirst()) {
                do {
                    list.add(cursor.getString(0))
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return list
        }
    val lastCapturedImage: ImageInfo?
        get() {
            val db = this.readableDatabase
            val query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + KEY_IMAGE_TIME + " DESC "
            val cursor = db.rawQuery(query, null)
            var imageInfo: ImageInfo? = null
            if (cursor.moveToFirst()) {
                imageInfo = ImageInfo(
                    cursor.getString(cursor.getColumnIndex(KEY_IMAGE_NAME)),
                    cursor.getLong(cursor.getColumnIndex(KEY_IMAGE_TIME)),
                    cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH)),
                    cursor.getDouble(cursor.getColumnIndex(KEY_IMAGE_LAT)),
                    cursor.getDouble(cursor.getColumnIndex(KEY_IMAGE_LON))
                )
            }
            cursor.close()
            db.close()
            return imageInfo
        }

    companion object {
        private const val TAG = "MainDbHelper"
        private const val DATABASE_NAME = "ImageInfoDb"
        private const val DATABASE_VERSION = 1
        private var mInstance: MainDbHelper? = null
        private const val TABLE_NAME = "IMAGE_DATA"
        private const val KEY_IMAGE_NAME = "NAME"
        private const val KEY_IMAGE_LAT = "LAT"
        private const val KEY_IMAGE_LON = "LON"
        private const val KEY_IMAGE_TIME = "TIME"
        private const val KEY_IMAGE_PATH = "PATH"
        private const val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
                + KEY_IMAGE_NAME + " TEXT PRIMARY KEY,"
                + KEY_IMAGE_LAT + " DOUBLE,"
                + KEY_IMAGE_LON + " DOUBLE,"
                + KEY_IMAGE_TIME + " DOUBLE,"
                + KEY_IMAGE_PATH + " TEXT"
                + ")")

        fun getInstance(context: Context): MainDbHelper? {
            if (mInstance == null) mInstance = MainDbHelper(context)
            return mInstance
        }
    }
}