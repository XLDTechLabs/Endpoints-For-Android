package com.xload.endpointsforandroid.modules.interactor

import android.app.Activity
import com.xload.endpointsforandroid.api.models.LinkedWallet
import com.xload.endpointsforandroid.api.models.WalletStatus
import com.xload.endpointsforandroid.utils.OnXLDConnectionListener
import com.xload.endpointsforandroid.utils.OnXLDListener
import com.xload.endpointsforandroid.utils.OnXLDStartListener

/**
 * @author John Paul Cas
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
interface XLDInteractor {

    /**
     * @param activity the  android.app.Activity
     * @param isDevelopment the environment selector
     * @param listener the event listener
     */
    fun init(
        activity: Activity,
        isDevelopment: Boolean = true,
        listener: OnXLDConnectionListener
    )

    /**
     * @param key the key
     * @param client the client
     * @param deviceId the device id
     * @param appId the app id
     */
    fun start(
        key: String,
        client: String,
        deviceId: String,
        appId: String,
        isDevelopment: Boolean = true,
        onStartListener: OnXLDStartListener
    )

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
        xldUserId: String,
        xldWalletAddress: String,
        xldOtp: String,
        partnerUserId: String,
        isDevelopment: Boolean = true,
        listener: OnXLDListener<LinkedWallet>
    )

    fun getWalletStatus(
        key: String,
        secret: String,
        xldUserId: String,
        isDevelopment: Boolean = true,
        listener: OnXLDListener<WalletStatus>
    )

}