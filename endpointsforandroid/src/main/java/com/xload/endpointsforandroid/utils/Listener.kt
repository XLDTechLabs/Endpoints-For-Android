package com.xload.endpointsforandroid.utils

/**
 * @author John Paul Cas
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */

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