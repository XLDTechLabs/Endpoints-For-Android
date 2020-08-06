package com.xload.endpointsforandroid.api.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author John Paul Cas
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
data class LinkedWallet (
    @SerializedName("xld_user_id")
    @Expose
    val xldUserId: String?,
    @SerializedName("xld_wallet_address")
    @Expose
    val xldWalletAddress: String?,
    @SerializedName("xld_otp")
    @Expose
    val xldOtp: Int?,
    @SerializedName("partner_user_id")
    @Expose
    val partnerUserId: String?,

    val linked: Boolean = false
)