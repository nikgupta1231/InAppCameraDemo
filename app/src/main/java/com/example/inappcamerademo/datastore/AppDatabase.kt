package com.example.inappcamerademo.datastore

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.inappcamerademo.model.ImageInfo

@Database(entities = [ImageInfo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun imagesDao(): ImagesDao

}