package com.xload.endpointsforandroid.utils

/**
 * @author John Paul Cas
 * Created by John Paul Cas on 03/08/2020.
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
object Encryptor {

    @JvmStatic
    fun encrypt(deviceId: String): String {
        val encrypt1 = deviceId.indexOf('_', ignoreCase = true)
        val encrypt2 = deviceId.indexOf('_', encrypt1 + 2, ignoreCase = true)
        return deviceId.substring(encrypt1 + 1, encrypt2)
    }
}