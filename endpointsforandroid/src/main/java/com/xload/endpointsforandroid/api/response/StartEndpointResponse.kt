package com.xload.endpointsforandroid.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author John Paul Cas
 * Created by John Paul Cas on 04/08/2020.
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
data class StartEndpointResponse (
    @SerializedName("xld_user_id")
    @Expose
    val xldUserId: String
)