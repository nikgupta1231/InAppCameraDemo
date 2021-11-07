package com.example.inappcamerademo.utils

object PermissionsUtil {

    private const val CAMERA_PERMISSION = 1
    private const val STORAGE_PERMISSION = 2
    private const val LOCATION_PERMISSION = 3

    val MANDATORY_PERMISSION = arrayOf(CAMERA_PERMISSION, STORAGE_PERMISSION)

    fun isPermissionAvailable(): Boolean {
        return false
    }

}