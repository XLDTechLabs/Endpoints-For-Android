package com.xload.endpointsforandroid.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author John Paul Cas
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
data class Error (
    @SerializedName("code")
    @Expose
    val code: Int?,
    @SerializedName("message")
    @Expose
    val message: String?
)