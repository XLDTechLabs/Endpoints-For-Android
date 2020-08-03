package com.xload.endpointsforandroid.utils

/**
 * @author John Paul Cas
 * Created by John Paul Cas on 30/07/2020.
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
sealed class XLDError {
    object NoReadPermission: XLDError()
    object XLoadAppNotLogin: XLDError()
    object XLoadNotInstall: XLDError()
    object UnknownError: XLDError()
}