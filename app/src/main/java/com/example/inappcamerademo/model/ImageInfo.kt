package com.example.inappcamerademo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImageInfo(
    val name: String,
    val timestamp: Long,
    val path: String,
    val lat: Double = 0.0,
    val lon: Double = 0.0
) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}
