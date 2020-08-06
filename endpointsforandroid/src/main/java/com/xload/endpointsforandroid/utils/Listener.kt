package com.xload.endpointsforandroid.utils

/**
 * @author John Paul Cas
 * Created by John Paul Cas on 30/07/2020.
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
interface OnXLDConnectionListener {
    fun onConnectionError(error: XLDError)
    fun onConnectionSuccess(key: String)
}

interface OnXLDStartListener {
    fun loading(isLoading: Boolean)
    fun error(error: String?)
    fun success(key: String)
}

interface OnXLDListener<in T> {
    fun loading(isLoading: Boolean)
    fun error(error: String?)
    fun success(result: T?)
}