package com.xload.endpointsforandroid.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat

/**
 * @author John Paul Cas
 * Created by John Paul Cas on 30/07/2020.
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
object Permissions {

    @JvmStatic
    fun hasReadExternalStoragePermission(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }

        return true
    }

    @JvmStatic
    fun isPackageInstalled(
        packageName: String,
        packageManager: PackageManager): Boolean {
        try {
            packageManager.getPackageInfo(packageName, 0)
            return true
        } catch (ignore: PackageManager.NameNotFoundException) {
            Log.d("DEBUG", " ${ignore.message}")
            return false
        }
    }
}