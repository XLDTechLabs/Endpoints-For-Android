package com.xload.endpointsforandroid.api.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author John Paul Cas
 * Created by John Paul Cas on 04/08/2020.
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
class EndpointStart(
    @SerializedName("device_id")
    @Expose
    val deviceId: String,
    @SerializedName("app_id")
    @Expose
    val appId: String
)