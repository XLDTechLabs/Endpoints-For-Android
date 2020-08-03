package com.xload.endpointsforandroid.modules

import android.app.Activity
import com.xload.endpointsforandroid.modules.state.XLDState
import com.xload.endpointsforandroid.modules.state.XLDStateImpl
import com.xload.endpointsforandroid.utils.OnXLDConnectionListener
import com.xload.endpointsforandroid.utils.Permissions
import com.xload.endpointsforandroid.utils.XLDError

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
        } else if (xldState.isXLoadAppInstalled(activity, isDevelopment)) {
            listener.onConnectionError(XLDError.XLoadNotInstall)
        }
    }
}