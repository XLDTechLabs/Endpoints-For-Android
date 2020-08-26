package com.xload.endpointsforandroid.api.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author John Paul Cas
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
data class LinkedWallet (
    @SerializedName("app_id")
    @Expose
    val appId: String?,
    @SerializedName("wallet_address")
    @Expose
    val walletAddress: String?,
    @SerializedName("otp")
    @Expose
    val otp: Int?,
    @SerializedName("partner_user_id")
    @Expose
    val partnerUserId: String?,

    val linked: Boolean = false,
    val xldUserId: String? = null
)