package com.xload.endpointsforandroid.modules

import android.app.Activity
import android.os.Environment
import android.util.Log
import com.google.gson.Gson
import com.xload.endpointsforandroid.modules.models.Generated
import com.xload.endpointsforandroid.modules.state.XLDState
import com.xload.endpointsforandroid.modules.state.XLDStateImpl
import com.xload.endpointsforandroid.utils.AppConstant
import com.xload.endpointsforandroid.utils.OnXLDConnectionListener
import com.xload.endpointsforandroid.utils.Permissions
import com.xload.endpointsforandroid.utils.XLDError
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

/**
 * @author John Paul Cas
 * Created by John Paul Cas on 30/07/2020.
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
object XLD {

    private val xldState: XLDState = XLDStateImpl()

    private var deviceId = ""

    @JvmStatic
    fun init(activity: Activity, isDevelopment: Boolean, listener: OnXLDConnectionListener) {
        if (!Permissions.hasReadExternalStoragePermission(activity)) {
            listener.onConnectionError(XLDError.NoReadPermission)
        } else if (!xldState.isXLoadAppInstalled(activity, isDevelopment)) {
            listener.onConnectionError(XLDError.XLoadNotInstall)
        } else {
            val folder = File(Environment.getExternalStorageDirectory(), AppConstant.FOLDER_NAME)

            if (folder.exists()) {
                val file = File(folder, AppConstant.FILE_NAME)

                try {
                    val result = FileInputStream(file).bufferedReader().use { it.readLine() }

                    val gson = Gson()
                    val key = gson.fromJson(result.toString(), Generated::class.java)
                    listener.onConnectionSuccess(xldState.encrypt(key.generated))
                } catch (ignore: FileNotFoundException) {
                    listener.onConnectionError(XLDError.XLoadAppNotLogin)
                } catch (error: IOException) {
                    Log.d("DEBUG", "Error ${error.message}")
                    listener.onConnectionError(XLDError.UnknownError(error.message))
                }
            } else {
                listener.onConnectionError(XLDError.XLoadAppNotLogin)
            }
        }
    }
}