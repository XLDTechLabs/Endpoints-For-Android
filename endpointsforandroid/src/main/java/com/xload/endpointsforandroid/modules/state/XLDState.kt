package com.xload.endpointsforandroid.modules.state

import android.content.Context

/**
 * @author John Paul Cas
 * Created by John Paul Cas on 03/08/2020.
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
interface XLDState {

    /**
     * Check if xload app installed
     */
    fun isXLoadAppInstalled(context: Context, isDevelopment: Boolean = true): Boolean

}