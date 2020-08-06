package com.xload.endpointsforandroid.modules.state

import android.content.Context
import android.util.Log
import com.xload.endpointsforandroid.utils.AppConstant
import com.xload.endpointsforandroid.utils.Encryptor
import com.xload.endpointsforandroid.utils.Permissions.isPackageInstalled

/**
 * @author John Paul Cas
 * Created by John Paul Cas on 03/08/2020.
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
class XLDStateImpl : XLDState {

    override fun isXLoadAppInstalled(context: Context, isDevelopment: Boolean): Boolean {
        return if (isDevelopment) {
            isXLoadAppDevelopmentInstalled(context)
        } else {
            isXLoadAppProductionInstalled(context)
        }
    }

    override fun encrypt(encrypted: String): String {
        return Encryptor.encrypt(encrypted)
    }

    /**
     * Check if the xload development app installed
     */
    private fun isXLoadAppDevelopmentInstalled(context: Context): Boolean {
        return isPackageInstalled(
            AppConstant.XLOAD_DEVELOPMENT_PACKAGE,
            context.packageManager
        )
    }

    /**
     * Check if production xload app installed
     */
    fun isXLoadAppProductionInstalled(context: Context): Boolean {
        return isPackageInstalled(
            AppConstant.XLOAD_PRODUCTION_PACKAGE,
            context.packageManager
        )
    }

}