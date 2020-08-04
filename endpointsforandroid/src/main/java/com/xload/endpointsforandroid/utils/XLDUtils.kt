package com.xload.endpointsforandroid.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * @author John Paul Cas
 * Created by John Paul Cas on 03/08/2020.
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
object XLDUtils {

    @JvmStatic
    fun redirectToXLoadApp(context: Context?, isDevelopment: Boolean = true) {
        val intent = context?.packageManager?.getLaunchIntentForPackage(
            if (isDevelopment) {
                AppConstant.XLOAD_DEVELOPMENT_PACKAGE
            } else {
                AppConstant.XLOAD_PRODUCTION_PACKAGE
            }
        )
        intent?.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context?.startActivity(intent)
    }

    @JvmStatic
    fun installXLoadApp(context: Context?) {
        try {
            gotoMarketLink(context, "${AppConstant.PLAY_MARKET}${AppConstant.XLOAD_PRODUCTION_PACKAGE}")
        } catch (ignore: ActivityNotFoundException) {
            gotoMarketLink(context,"${AppConstant.GOOGLE_PLAY_MARKET}${AppConstant.XLOAD_PRODUCTION_PACKAGE}")
        }
    }

    @JvmStatic
    private fun gotoMarketLink(context: Context?, link: String) {
        val intent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(intent)
    }

}