package com.xload.endpointsforandroid.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author John Paul Cas
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
class LinkWalletResponse(
    @SerializedName("xld_user_id")
    @Expose
    val xldUserId: String?,
    @SerializedName("partner_user_id")
    @Expose
    val partnerUserId: String?,
    @SerializedName("linked")
    @Expose
    val linked: Boolean,
    @SerializedName("error")
    @Expose
    val error: Error?
)