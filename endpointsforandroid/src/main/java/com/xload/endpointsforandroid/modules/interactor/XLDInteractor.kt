package com.xload.endpointsforandroid.modules.interactor

import com.xload.endpointsforandroid.api.models.LinkedWallet
import com.xload.endpointsforandroid.api.models.WalletStatus
import com.xload.endpointsforandroid.utils.OnXLDListener

/**
 * @author John Paul Cas
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
interface XLDInteractor {

    fun conversion(
        key: String,
        secret: String,
        appId: String,
        isDevelopment: Boolean = true,
        listener: OnXLDListener<Double>
    )

    fun linkWallet(
        key: String,
        secret: String,
        appId: String,
        walletAddress: String,
        otp: Int,
        partnerUserId: String,
        isDevelopment: Boolean = true,
        listener: OnXLDListener<LinkedWallet>
    )

    fun getWallet(
        key: String,
        secret: String,
        xldUserId: String,
        isDevelopment: Boolean = true,
        listener: OnXLDListener<WalletStatus>
    )

}